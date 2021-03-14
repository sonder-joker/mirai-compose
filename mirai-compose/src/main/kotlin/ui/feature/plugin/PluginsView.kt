package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.*
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.ui.common.PluginCommandView
import com.youngerhousea.miraicompose.ui.common.PluginDataView
import com.youngerhousea.miraicompose.ui.common.PluginDescription
import com.youngerhousea.miraicompose.utils.ChildWrapper
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.CrossFade
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.registeredCommands
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin


class PluginV(component: ComponentContext, val plugins: List<Plugin>) : Component, ComponentContext by component {

    private val router = router<Configuration, Component>(
        initialConfiguration = Configuration.List,
        key = "PluginRouter",
        handleBackButton = true,
        componentFactory = { configuration: Configuration, ComponentContext ->
            when (configuration) {
                is Configuration.List -> {
                    PluginList(
                        ComponentContext,
                        plugins,
                        onItemSelected = ::onItemSelected,
                    )
                }
                is Configuration.Detailed -> {
                    when (configuration.plugin) {
                        is JvmPlugin -> {
                            PluginDetailed(
                                ComponentContext,
                                configuration.plugin,
                                onExit = ::onExit
                            )
                        }
                        else -> {
                            TODO()
                        }
                    }
                }
            }
        }
    )

    private fun onItemSelected(plugin: Plugin) {
        router.push(Configuration.Detailed(plugin))
    }

    private fun onExit() {
        router.pop()
    }

    @Composable
    override fun render() {
        Children(router.state, animation = CrossFade()) { child, _ ->
            child.render()
        }
    }

    sealed class Configuration : Parcelable {
        object List : Configuration()
        class Detailed(val plugin: Plugin) : Configuration()
    }

}

class PluginList(
    componentContext: ComponentContext,
    val plugins: List<Plugin>,
    val onItemSelected: (plugin: Plugin) -> Unit
) : Component, ComponentContext by componentContext {


    @Composable
    override fun render() {
        LazyVerticalGrid(
            cells = GridCells.Adaptive(300.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp)
        ) {
            items(plugins) { plugin ->
                Card(
                    Modifier
                        .padding(10.dp)
                        .clickable(onClick = { onItemSelected(plugin) })
                        .requiredHeight(150.dp)
                        .fillMaxWidth(),
                    backgroundColor= Color(0xff979595)
                ) {
                    PluginDescription(plugin, Modifier.padding(10.dp))
                }
            }
        }
    }
}


class PluginDetailed(
    componentContext: ComponentContext,
    val plugin: JvmPlugin,
    val onExit: () -> Unit
) : Component, ComponentContext by componentContext {

    sealed class Setting : Parcelable {
        object Command : Setting()
        object Data : Setting()
    }

    private val router: Router<Setting, Component> = router<Setting, Component>(
        initialConfiguration = Setting.Data,
        handleBackButton = true,
        key = "PluginDetailedRouter",
        componentFactory = { configuration, componentContext ->
            when (configuration) {
                is Setting.Data -> {
                    DataWindow(componentContext, plugin, ::onClick)
                }
                is Setting.Command -> {
                    CommandWindow(componentContext, plugin.registeredCommands, ::onReturn)
                }
            }
        }
    )

    private fun onClick() {
        router.push(Setting.Command)
    }

    private fun onReturn() {
        router.pop()
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    override fun render() {
        Card(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                Icon(Icons.Default.KeyboardArrowLeft, null, Modifier.clickable(onClick = onExit))
                PluginDescription(plugin, Modifier.padding(start = 20.dp))
                Divider(thickness = 5.dp)
                Children(router.state, animation = { child, configuration, function ->
                    AnimatedVisibility(configuration is Setting.Data) {
                        function(child, configuration)
                    }
                }) { child, _ ->
                    child.render()
                }
            }
        }
    }

}

class DataWindow(
    componentContext: ComponentContext,
    private val plugin: JvmPlugin,
    private val onRightClick: () -> Unit
) : Component, ComponentContext by componentContext {
    @Composable
    override fun render() {
        Row(Modifier.fillMaxSize()) {
            PluginDataView(plugin = plugin, modifier = Modifier.weight(6f))
            SwitchArrow(
                isLeft = false,
                onClick = onRightClick, modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

class CommandWindow(
    componentContext: ComponentContext,
    private val command: List<Command>,
    private val onLeftClick: () -> Unit
) : Component, ComponentContext by componentContext {
    @Composable
    override fun render() {
        Row(Modifier.fillMaxSize()) {
            SwitchArrow(
                isLeft = true,
                onClick = onLeftClick, modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            PluginCommandView(registeredCommands = command, modifier = Modifier.weight(6f))
        }
    }
}

@Composable
fun SwitchArrow(isLeft: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Icon(
        if (isLeft) Icons.Default.ArrowBack else Icons.Default.ArrowForward,
        null,
        modifier.padding(horizontal = 40.dp).clickable(onClick = onClick)
    )
}
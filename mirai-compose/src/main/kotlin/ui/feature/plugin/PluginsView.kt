package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.*
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.console.dataWithConfig
import com.youngerhousea.miraicompose.ui.common.PluginCommandView
import com.youngerhousea.miraicompose.ui.common.PluginDataView
import com.youngerhousea.miraicompose.ui.common.PluginDescription
import com.youngerhousea.miraicompose.utils.Component
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.registeredCommands
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.plugin.Plugin


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
                    PluginDetailed(
                        ComponentContext,
                        configuration.plugin,
                        onExit = ::onExit
                    )
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
        Children(router.state) { child, _ ->
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
            cells = GridCells.Adaptive(400.dp),
            Modifier.fillMaxSize(),
        ) {
            items(plugins) { plugin ->
                Card(
                    Modifier
                        .padding(40.dp)
                        .clickable(onClick = { onItemSelected(plugin) })
                        .requiredHeight(200.dp)
                        .fillMaxWidth()
                ) {
                    PluginDescription(plugin)
                }
            }
        }
    }
}


class PluginDetailed(
    componentContext: ComponentContext,
    val plugin: Plugin,
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
                is Setting.Command -> {
                    CommandWindow(componentContext, plugin.registeredCommands, ::onClick)
                }
                is Setting.Data -> {
                    DataWindow(componentContext, plugin.dataWithConfig, ::onReturn)
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

    @Composable
    override fun render() {
        Children(router.state) { child, _ ->
            Card(Modifier.fillMaxSize()) {
                Column {
                    Icon(Icons.Default.KeyboardArrowLeft, null, Modifier.clickable(onClick = onExit))
                    PluginDescription(plugin)
                    Divider(thickness = 5.dp)
                    child.render()
                }
            }
        }
    }
}

class CommandWindow(
    componentContext: ComponentContext,
    private val command: List<Command>,
    private val onClick: () -> Unit
) : Component, ComponentContext by componentContext {
    @Composable
    override fun render() {
        Column(Modifier.fillMaxSize()) {
            PluginCommandView(registeredCommands = command)
            Icon(Icons.Default.KeyboardArrowLeft, null, Modifier.clickable(onClick = onClick))
        }
    }
}


class DataWindow(
    componentContext: ComponentContext,
    private val pluginDatas: List<PluginData>,
    private val onReturn: () -> Unit
) : Component, ComponentContext by componentContext {
    @Composable
    override fun render() {
        Column(Modifier.fillMaxSize()) {
            PluginDataView(pluginDatas = pluginDatas)
            Icon(Icons.Default.KeyboardArrowLeft, null, Modifier.clickable(onClick = onReturn))
        }
    }
}

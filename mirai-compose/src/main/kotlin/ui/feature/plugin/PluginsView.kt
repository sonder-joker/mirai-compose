package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.animation.animateContentSize
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.*
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.ui.common.PluginCommandView
import com.youngerhousea.miraicompose.ui.common.PluginDataView
import com.youngerhousea.miraicompose.ui.common.PluginDescriptionCard
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.CrossFade
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.registeredCommands
import net.mamoe.mirai.console.plugin.*
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.description
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
                    PluginDetailed(
                        ComponentContext,
                        configuration.plugin as JvmPlugin,
                        onExit = ::onExit
                    )
                }
            }
        })

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
                PluginDescriptionCard(plugin, onItemSelected = onItemSelected)
            }
        }
    }

}


class PluginDetailed(
    componentContext: ComponentContext,
    private val plugin: JvmPlugin,
    val onExit: () -> Unit
) : Component, ComponentContext by componentContext {

    sealed class Setting : Parcelable {
        object Description : Setting()
        object Command : Setting()
        object Data : Setting()
    }

    private val router: Router<Setting, Component> = router<Setting, Component>(
        initialConfiguration = Setting.Data,
        handleBackButton = true,
        key = "PluginDetailedRouter",
        componentFactory = { configuration, componentContext ->
            when (configuration) {
                is Setting.Description ->
                    DescriptionWindow(componentContext, plugin)
                is Setting.Data ->
                    DataWindow(componentContext, plugin)
                is Setting.Command ->
                    CommandWindow(componentContext, plugin.registeredCommands)
            }
        }
    )

    private inline val Plugin.annotatedName: AnnotatedString
        get() = with(AnnotatedString.Builder()) {
            pushStyle(SpanStyle(fontWeight = FontWeight.Medium, color = Color.Black, fontSize = 20.sp))
            append(name.ifEmpty { "Unknown" })
            pop()
            toAnnotatedString()
        }

    @Composable
    override fun render() {

        Column(Modifier.fillMaxSize().animateContentSize()) {
            Box(Modifier.fillMaxWidth().requiredHeight(34.dp), contentAlignment = Alignment.CenterStart) {
                Icon(Icons.Default.KeyboardArrowLeft, null, Modifier.clickable(onClick = onExit))
                Text(
                    plugin.annotatedName, textAlign = TextAlign.Center, maxLines = 1, modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }
            var index by remember(plugin) { mutableStateOf(0) }
            TabRow(index) {
                Tab(
                    selectedContentColor = Color.Black,
                    text = { Text("Description") },
                    selected = index == 0,
                    onClick = { index = 0; router.push(Setting.Description) })
                Tab(
                    selectedContentColor = Color.Black,
                    text = { Text("Data") },
                    selected = index == 1,
                    onClick = { index = 1;router.push(Setting.Data) })
                Tab(
                    selectedContentColor = Color.Black,
                    text = { Text("Command") },
                    selected = index == 2,
                    onClick = { index = 2; router.push(Setting.Command) })
            }
            Children(router.state) { child, _ ->
                child.render()
            }
        }
    }

}


class DescriptionWindow(
    componentContext: ComponentContext,
    val plugin: Plugin
) : Component, ComponentContext by componentContext {
    private val Plugin.annotatedDescription
        get() = with(AnnotatedString.Builder()) {
            pushStyle(SpanStyle(fontWeight = FontWeight.Medium, color = Color.Black, fontSize = 20.sp))
            append("Name:${name.ifEmpty { "Unknown" }}\n")
            append("ID:$id\n")
            append("Version:${version}\n")
            append("Info:${info.ifEmpty { "None" }}\n")
            append("Author:$author\n")
            append("Dependencies:$dependencies")
            pop()
            toAnnotatedString()
        }

    @Composable
    override fun render() {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(plugin.annotatedDescription)
        }
    }

}


class DataWindow(
    componentContext: ComponentContext,
    private val plugin: JvmPlugin,
) : Component, ComponentContext by componentContext {
    @Composable
    override fun render() {
        PluginDataView(plugin = plugin)
    }
}

class CommandWindow(
    componentContext: ComponentContext,
    private val command: List<Command>,
) : Component, ComponentContext by componentContext {
    @Composable
    override fun render() {
        PluginCommandView(registeredCommands = command)
    }
}

//@Composable
//fun SwitchArrow(isLeft: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
//    Icon(
//        if (isLeft) Icons.Default.ArrowBack else Icons.Default.ArrowForward,
//        null,
//        modifier.padding(horizontal = 40.dp).clickable(onClick = onClick)
//    )
//}
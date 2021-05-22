package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.*
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.console.AccessibleHolder
import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent
import net.mamoe.mirai.console.ConsoleFrontEndImplementation
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.registeredCommands
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin


/**
 * Jvm插件的页面
 *
 * @see DetailedDescription
 * @see DetailedData
 * @see DetailedCommand
 */
@ConsoleFrontEndImplementation
class CJvmPlugin(
    componentContext: ComponentContext,
    val plugin: JvmPlugin,
) : ComponentContext by componentContext, AccessibleHolder by MiraiCompose {

    private val router: Router<Setting, Component> = router(
        initialConfiguration = Setting.Description,
        handleBackButton = true,
        key = "PluginDetailedRouter",
        childFactory = { configuration, componentContext ->
            when (configuration) {
                is Setting.Description ->
                    DetailedDescription(componentContext, plugin).asComponent { DetailedDescriptionUi(it) }
                is Setting.Data ->
                    DetailedData(
                        componentContext,
                        data = plugin.data + plugin.config
                    ).asComponent { DetailedDataUi(it) }
                is Setting.Command ->
                    DetailedCommand(componentContext, plugin.registeredCommands).asComponent { DetailedCommandUi(it) }
            }
        }
    )

    val state get() = router.state


    fun onDescriptionClick() {
        router.push(Setting.Description)
    }

    fun onDataClick() {
        router.push(Setting.Data)
    }

    fun onCommandClick() {
        router.push(Setting.Command)
    }

    sealed class Setting : Parcelable {
        object Description : Setting()
        object Command : Setting()
        object Data : Setting()
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun CJvmPluginUi(CJvmPlugin: CJvmPlugin) {
    Column {
        var index by remember { mutableStateOf(0) }
        TabRow(index) {
            Tab(
                selectedContentColor = Color.Black,
                text = { Text("Description") },
                selected = index == 0,
                onClick = {
                    CJvmPlugin.onDescriptionClick()
                    index = 0
                }
            )
            Tab(
                selectedContentColor = Color.Black,
                text = { Text("Data") },
                selected = index == 1,
                onClick = {
                    CJvmPlugin.onDataClick()
                    index = 1
                }
            )
            Tab(
                selectedContentColor = Color.Black,
                text = { Text("Command") },
                selected = index == 2,
                onClick = {
                    CJvmPlugin.onCommandClick()
                    index = 2
                }
            )
        }

        Children(CJvmPlugin.state, crossfade()) { child ->
            child.instance()
        }
    }
}

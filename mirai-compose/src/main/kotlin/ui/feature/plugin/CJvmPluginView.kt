package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.*
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.slide
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.console.AccessibleHolder
import com.youngerhousea.miraicompose.future.getGlobal
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.registeredCommands
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin


/**
 * Jvm插件的页面
 *
 * @see DetailedDescription
 * @see DetailedData
 * @see DetailedCommand
 */
class CJvmPlugin(
    componentContext: ComponentContext,
    val plugin: JvmPlugin,
) : ComponentContext by componentContext, AccessibleHolder by getGlobal() {
    private var _index by mutableStateOf(0)

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

    val index get() = _index

    fun onDescriptionClick() {
        _index = 0
        router.push(Setting.Description)
    }

    fun onDataClick() {
        _index = 1
        router.push(Setting.Data)
    }

    fun onCommandClick() {
        _index = 2
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
fun CJvmPluginUi(CJvmPlugin: CJvmPlugin) = Column {
    TabRow(CJvmPlugin.index) {
        Tab(
            selectedContentColor = Color.Black,
            text = { Text("Description") },
            selected = CJvmPlugin.index == 0,
            onClick = CJvmPlugin::onDescriptionClick
        )
        Tab(
            selectedContentColor = Color.Black,
            text = { Text("Data") },
            selected = CJvmPlugin.index == 1,
            onClick = CJvmPlugin::onDataClick
        )
        Tab(
            selectedContentColor = Color.Black,
            text = { Text("Command") },
            selected = CJvmPlugin.index == 2,
            onClick = CJvmPlugin::onCommandClick
        )
    }
    Children(CJvmPlugin.state, slide()) { child ->
        child.instance()
    }
}

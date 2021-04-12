package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.*
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.console.AccessibleHolder
import com.youngerhousea.miraicompose.ui.common.EditView
import com.youngerhousea.miraicompose.ui.common.annotatedDescription
import com.youngerhousea.miraicompose.ui.common.annotatedExplain
import com.youngerhousea.miraicompose.ui.common.simpleDescription
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent
import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.registeredCommands
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin


class CJvmPlugin(
    componentContext: ComponentContext,
    val plugin: JvmPlugin,
    accessibleHolder: AccessibleHolder
) : ComponentContext by componentContext, AccessibleHolder by accessibleHolder {

    sealed class Setting : Parcelable {
        object Description : Setting()
        object Command : Setting()
        object Data : Setting()
    }

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
                        coroutineScope = plugin,
                        data = plugin.data + plugin.config
                    ).asComponent { DetailedDataUi(it) }
                is Setting.Command ->
                    DetailedCommand(componentContext, plugin.registeredCommands).asComponent { DetailedCommandUi(it) }
            }
        }
    )

    val state get() = router.state

    private var _index by mutableStateOf(0)

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
    Children(CJvmPlugin.state, crossfade()) { child ->
        child.instance()
    }
}

class DetailedDescription(
    componentContext: ComponentContext,
    val plugin: Plugin
) : ComponentContext by componentContext

@Composable
fun DetailedDescriptionUi(detailedDescription: DetailedDescription) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(detailedDescription.plugin.annotatedDescription)
    }
}


class DetailedData(
    componentContext: ComponentContext,
    val coroutineScope: CoroutineScope,
    val data: List<PluginData>,
) : ComponentContext by componentContext

@Composable
fun DetailedDataUi(detailedData: DetailedData) =
    LazyColumn(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(detailedData.data) { pluginData ->
            Text(pluginData.annotatedExplain, Modifier.padding(bottom = 40.dp))
            EditView(pluginData, detailedData.coroutineScope)
        }
    }


class DetailedCommand(
    componentContext: ComponentContext,
    internal val command: List<Command>,
) : ComponentContext by componentContext

@Composable
fun DetailedCommandUi(detailedCommand: DetailedCommand) =
    LazyColumn {
        items(detailedCommand.command) { registeredCommand ->
            Text(registeredCommand.simpleDescription)
            Spacer(Modifier.height(20.dp))
        }
    }
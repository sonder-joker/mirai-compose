package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.Router
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.ui.common.*
import com.youngerhousea.miraicompose.ui.common.EditView
import com.youngerhousea.miraicompose.ui.common.annotatedExplain
import com.youngerhousea.miraicompose.ui.common.annotatedName
import com.youngerhousea.miraicompose.ui.common.simpleDescription
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.registeredCommands
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.plugin.*
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin


class PluginDetailed(
    componentContext: ComponentContext,
    val plugin: Plugin,
    val data: List<PluginData>,
    val onExit: () -> Unit
) : ComponentContext by componentContext {

    sealed class Setting : Parcelable {
        object Description : Setting()
        object Command : Setting()
        object Data : Setting()
    }

    private val router: Router<Setting, Component> = router(
        initialConfiguration = Setting.Description,
        handleBackButton = true,
        key = "PluginDetailedRouter",
        componentFactory = { configuration, componentContext ->
            when (configuration) {
                is Setting.Description ->
                    DetailedDescription(componentContext, plugin).asComponent { DetailedDescriptionUi(it) }
                is Setting.Data ->
//                    when (plugin) {
//                        is JvmPlugin ->
                    DetailedData(componentContext, plugin as JvmPlugin, data).asComponent { DetailedDataUi(it) }
//                        else ->
//                            error("Other Plugin!")
//                    }
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

@Composable
fun PluginDetailedUi(pluginDetailed: PluginDetailed) = Column {
    Box(Modifier.fillMaxWidth().requiredHeight(34.dp), contentAlignment = Alignment.CenterStart) {
        Icon(Icons.Default.KeyboardArrowLeft, null, Modifier.clickable(onClick = pluginDetailed.onExit))
        Text(
            pluginDetailed.plugin.annotatedName,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.align(Alignment.Center)
        )
    }

    TabRow(pluginDetailed.index) {
        Tab(
            selectedContentColor = Color.Black,
            text = { Text("Description") },
            selected = pluginDetailed.index == 0,
            onClick = pluginDetailed::onDescriptionClick
        )
        Tab(
            selectedContentColor = Color.Black,
            text = { Text("Data") },
            selected = pluginDetailed.index == 1,
            onClick = pluginDetailed::onDataClick
        )
        Tab(
            selectedContentColor = Color.Black,
            text = { Text("Command") },
            selected = pluginDetailed.index == 2,
            onClick = pluginDetailed::onCommandClick
        )
    }
    Children(pluginDetailed.state) { child, _ ->
        child()
    }
}

class DetailedDescription(
    componentContext: ComponentContext,
    val plugin: Plugin
) : ComponentContext by componentContext {

}

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
    internal val plugin: JvmPlugin,
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
            EditView(pluginData, detailedData.plugin)
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
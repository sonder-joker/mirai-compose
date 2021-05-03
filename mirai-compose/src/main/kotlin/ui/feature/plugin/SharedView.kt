package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.ui.common.annotatedDescription
import com.youngerhousea.miraicompose.ui.common.annotatedExplain
import com.youngerhousea.miraicompose.ui.common.simpleDescription
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.console.command.executeCommand
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.yamlkt.Yaml

/**
 * 插件描述
 */
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

/**
 * 插件数据
 * Only for jvm
 */
class DetailedData(
    componentContext: ComponentContext,
    val data: List<PluginData>,
) : ComponentContext by componentContext

@Composable
fun DetailedDataUi(detailedData: DetailedData) {
    LazyColumn(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(detailedData.data) { pluginData ->
            Text(pluginData.annotatedExplain, Modifier.padding(bottom = 40.dp))
            EditView(pluginData)
        }
    }
}

@Composable
private fun EditView(pluginData: PluginData) {
    var value by remember(pluginData) {
        mutableStateOf(
            Yaml.encodeToString(
                pluginData.updaterSerializer,
                Unit
            )
        )
    }

    var textField by remember(pluginData) { mutableStateOf(TextFieldValue(value)) }

    Row(
        Modifier.fillMaxWidth().padding(bottom = 40.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        TextField(textField, {
            textField = it
        })
        Button(
            {
                runCatching {
                    Yaml.decodeFromString(pluginData.updaterSerializer, textField.text)
                }.onSuccess {
                    value = textField.text
                }
            },
            Modifier
                .requiredWidth(100.dp)
                .background(MaterialTheme.colors.background)
        ) {
            Text("修改")
        }
    }
}


/**
 *  插件指令
 */
class DetailedCommand(
    componentContext: ComponentContext,
    val commands: List<Command>,
) : ComponentContext by componentContext

@Composable
fun DetailedCommandUi(detailedCommand: DetailedCommand) {
    LazyColumn {
        items(detailedCommand.commands) { registeredCommand ->
            CommandF(registeredCommand)
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun CommandF(command: Command) {
    Column {
        Text(command.simpleDescription)
        Text(command.permission.id.toString())
        Text(command.permission.parent.id.toString())
        Button(onClick = {
        }) {
            Text("Quick execute")
        }
    }
}
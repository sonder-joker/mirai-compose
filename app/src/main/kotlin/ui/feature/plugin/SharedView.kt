package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.theme.R
import com.youngerhousea.miraicompose.ui.common.annotatedDescription
import com.youngerhousea.miraicompose.ui.common.annotatedExplain
import com.youngerhousea.miraicompose.ui.common.simpleDescription
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.yamlkt.Yaml
import java.awt.Desktop
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.readText
import kotlin.io.path.writeText

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

    var textField by remember(pluginData) { mutableStateOf(value) }

    Row(
        Modifier.fillMaxWidth().padding(bottom = 40.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        OutlinedTextField(textField, {
            textField = it
        })
        Button(
            {
                runCatching {
                    Yaml.decodeFromString(pluginData.updaterSerializer, textField)
                }.onSuccess {
                    value = textField
                }
            },
            Modifier
                .requiredWidth(100.dp)
                .background(MaterialTheme.colors.background)
        ) {
            Text("Change")
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
            Column {
                Text(registeredCommand.simpleDescription)
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}


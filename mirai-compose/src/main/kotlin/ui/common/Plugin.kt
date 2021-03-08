package com.youngerhousea.miraicompose.ui.common

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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youngerhousea.miraicompose.utils.items
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.Command.Companion.allNames
import net.mamoe.mirai.console.data.*
import net.mamoe.mirai.console.plugin.*
import net.mamoe.yamlkt.Yaml

private val yaml = Yaml.default

private inline val Plugin.annotatedName: AnnotatedString
    get() = with(AnnotatedString.Builder()) {
        pushStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp))
        append(this@annotatedName.name.ifEmpty { "未知" })
        pop()
        toAnnotatedString()
    }


private inline val Plugin.annotatedAuthor: AnnotatedString
    get() = with(AnnotatedString.Builder()) {
        pushStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
        append("简介")
        pop()
        append(this@annotatedAuthor.author.ifEmpty { "未知" })
        toAnnotatedString()
    }

private inline val Plugin.annotatedInfo: AnnotatedString
    get() = with(AnnotatedString.Builder()) {
        pushStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
        append("作者")
        pop()
        append(this@annotatedInfo.info.ifEmpty { "未知" })
        toAnnotatedString()
    }

private inline val Plugin.annotatedSimple: AnnotatedString
    get() = with(AnnotatedString.Builder()) {
        append(this@annotatedSimple.annotatedName)
        append('\n')
        append(this@annotatedSimple.annotatedInfo)
        append('\n')
        append(this@annotatedSimple.annotatedAuthor)
        toAnnotatedString()
    }

@Composable
internal fun PluginDescription(plugin: Plugin) =
    Text(plugin.annotatedSimple, Modifier.padding(start = 20.dp))

@Composable
internal fun PluginDataView(modifier: Modifier = Modifier, pluginDatas: List<PluginData>) =
    LazyColumn(
        modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(pluginDatas) { pluginData ->
            PluginDataExplanationView(pluginData)
            EditView(pluginData)
        }
    }

private inline val PluginData.annotatedExplain: AnnotatedString
    get() = with(AnnotatedString.Builder()) {
        pushStyle(SpanStyle(fontSize = 20.sp))
        append(
            when (this@annotatedExplain) {
                is AutoSavePluginConfig -> "自动保存配置"
                is ReadOnlyPluginConfig -> "只读配置"
                is AutoSavePluginData -> "自动保存数据"
                is ReadOnlyPluginData -> "只读数据"
                else -> "未知"
            }
        )
        append(':')
        append(this@annotatedExplain.saveName)
        toAnnotatedString()
    }

@Composable
private fun PluginDataExplanationView(pluginData: PluginData) =
    Text(pluginData.annotatedExplain, Modifier.padding(bottom = 40.dp))

@Composable
private fun EditView(pluginData: PluginData) {
    var value by remember(pluginData) {
        mutableStateOf(
            yaml.encodeToString(
                pluginData.updaterSerializer,
                Unit
            )
        )
    }


    var textField by remember(pluginData) { mutableStateOf(TextFieldValue(value)) }

    var isSuccess by remember(pluginData) { mutableStateOf(true) }

    Row(
        Modifier.fillMaxWidth().padding(bottom = 40.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        TextField(textField, {
            textField = it
        })
        Button(
            {
                kotlin.runCatching {
                    yaml.decodeFromString(pluginData.updaterSerializer, textField.text)
                }.onSuccess {
                    value = textField.text
                    isSuccess = true
                }.onFailure {
                    it.printStackTrace()
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

@Composable
internal fun PluginCommandView(modifier: Modifier = Modifier, registeredCommands: List<Command>) =
    LazyColumn(modifier) {
        items(registeredCommands) { registeredCommand ->
            Text(registeredCommand.allNames.joinToString { " " } + '\n' + registeredCommand.usage + '\n' + registeredCommand.description)
        }
    }




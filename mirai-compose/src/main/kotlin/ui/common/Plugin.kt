package com.youngerhousea.miraicompose.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlignHorizontalCenter
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.materialIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youngerhousea.miraicompose.console.dataWithConfig
import com.youngerhousea.miraicompose.theme.ResourceImage
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.Command.Companion.allNames
import net.mamoe.mirai.console.data.*
import net.mamoe.mirai.console.plugin.*
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.yamlkt.Yaml

private val yaml = Yaml.default

private inline val Plugin.annotatedName: AnnotatedString
    get() = with(AnnotatedString.Builder()) {
        pushStyle(SpanStyle(fontWeight = FontWeight.Medium, color = Color.White, fontSize = 20.sp))
        append(name.ifEmpty { "Unknown" })
        pop()
        toAnnotatedString()
    }


private inline val Plugin.annotatedAuthor: AnnotatedString
    get() = with(AnnotatedString.Builder()) {
        pushStyle(SpanStyle(color = Color.White, fontSize = 13.sp))
        append("Author:")
        append(this@annotatedAuthor.author.ifEmpty { "Unknown" })
        toAnnotatedString()
    }

private inline val Plugin.annotatedInfo: AnnotatedString
    get() = with(AnnotatedString.Builder()) {
        pushStyle(SpanStyle(color = Color.White, fontSize = 13.sp))
        append("Info:")
        append(this@annotatedInfo.info.ifEmpty { "Unknown" })
        toAnnotatedString()
    }

//private inline val Plugin.annotatedSimple: AnnotatedString
//    get() = with(AnnotatedString.Builder()) {
//        append(this@annotatedSimple.annotatedName)
//        append('\n')
//        append(this@annotatedSimple.annotatedAuthor)
//        append('\n')
//        append(this@annotatedSimple.annotatedInfo)
//        toAnnotatedString()
//    }

private inline val Plugin.annotatedKind: AnnotatedString
    get() = with(AnnotatedString.Builder()) {
        pushStyle(SpanStyle(color = Color.White, fontSize = 13.sp))
        append(
            when (this@annotatedKind) {
                is JavaPlugin -> {
                    "Java"
                }
                is KotlinPlugin -> {
                    "Kotlin"
                }
                else -> {
                    "Help"
                }
            }
        )
        toAnnotatedString()
    }

private val Plugin.kindIcon: ImageVector
    get() =
        when (this) {
            is JavaPlugin -> {
                ResourceImage.java
            }
            is KotlinPlugin -> {
                ResourceImage.kotlin
            }
            else -> {
                Icons.Default.Help
            }
        }


@Composable
internal fun PluginDescription(plugin: Plugin, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(plugin.annotatedName, overflow = TextOverflow.Ellipsis, maxLines = 1)
        Spacer(Modifier.height(20.dp))
        Text(plugin.annotatedAuthor, overflow = TextOverflow.Ellipsis)
        Spacer(Modifier.height(10.dp))
        Text(plugin.annotatedInfo, overflow = TextOverflow.Ellipsis)
        Spacer(Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(plugin.kindIcon, null, tint = Color.White)
            Text(plugin.annotatedKind)
        }
    }
}

@Composable
internal fun PluginDataView(modifier: Modifier = Modifier, plugin: JvmPlugin) =
    LazyColumn(
        modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(plugin.dataWithConfig) { pluginData ->
            PluginDataExplanationView(pluginData)
            EditView(pluginData, plugin)
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
private fun EditView(pluginData: PluginData, plugin: JvmPlugin) {
    var value by remember(pluginData) {
        mutableStateOf(
            yaml.encodeToString(
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
                plugin.launch {
                    kotlin.runCatching {
                        yaml.decodeFromString(pluginData.updaterSerializer, textField.text)
                    }.onSuccess {
                        value = textField.text
                    }.onFailure {
                        it.printStackTrace()
                    }
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
            Text(registeredCommand.simpleDescription)
            Spacer(Modifier.height(20.dp))
        }
    }

private inline val Command.simpleDescription: AnnotatedString
    get() =
        with(AnnotatedString.Builder()) {
            append(allNames.joinToString { " " })
            append('\n')
            append(usage)
            append('\n')
            append(description)
            toAnnotatedString()
        }




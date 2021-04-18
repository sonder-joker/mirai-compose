package com.youngerhousea.miraicompose.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youngerhousea.miraicompose.theme.ResourceImage
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.Command.Companion.allNames
import net.mamoe.mirai.console.data.*
import net.mamoe.mirai.console.plugin.*
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.yamlkt.Yaml

internal inline val Plugin.annotatedName: AnnotatedString
    get() = with(AnnotatedString.Builder()) {
        pushStyle(SpanStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp))
        append(name.ifEmpty { "Unknown" })
        pop()
        toAnnotatedString()
    }

private inline val Plugin.annotatedAuthor: AnnotatedString
    get() = with(AnnotatedString.Builder()) {
        pushStyle(SpanStyle(fontSize = 13.sp))
        append("Author:")
        append(author.ifEmpty { "Unknown" })
        toAnnotatedString()
    }

private inline val Plugin.annotatedInfo: AnnotatedString
    get() = with(AnnotatedString.Builder()) {
        pushStyle(SpanStyle(fontSize = 13.sp))
        append("Info:")
        append(info.ifEmpty { "Unknown" })
        toAnnotatedString()
    }

private inline val Plugin.annotatedKind: AnnotatedString
    get() = with(AnnotatedString.Builder()) {
        pushStyle(SpanStyle(fontSize = 13.sp))
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

private val Plugin.languageIcon: ImageVector
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

val Plugin.annotatedDescription: AnnotatedString
    get() = with(AnnotatedString.Builder()) {
        pushStyle(SpanStyle(fontWeight = FontWeight.Medium, color = Color.Black, fontSize = 20.sp))
        append("Name:${this@annotatedDescription.name.ifEmpty { "Unknown" }}\n")
        append("ID:${this@annotatedDescription.id}\n")
        append("Version:${this@annotatedDescription.version}\n")
        append("Info:${this@annotatedDescription.info.ifEmpty { "None" }}\n")
        append("Author:${this@annotatedDescription.author}\n")
        append("Dependencies:${this@annotatedDescription.dependencies}")
        pop()
        toAnnotatedString()
    }

@Composable
internal fun PluginDescription(plugin: Plugin, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(plugin.annotatedName, overflow = TextOverflow.Ellipsis, maxLines = 1)
        Spacer(Modifier.height(20.dp))
        Text(plugin.annotatedAuthor, overflow = TextOverflow.Ellipsis, maxLines = 1)
        Spacer(Modifier.height(10.dp))
        Text(plugin.annotatedInfo, overflow = TextOverflow.Ellipsis, maxLines = 1)
        Spacer(Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(plugin.languageIcon, null)
            Text(plugin.annotatedKind)
        }
    }
}


internal inline val PluginData.annotatedExplain: AnnotatedString
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
internal fun EditView(pluginData: PluginData) {
    var value by remember(pluginData) {
        mutableStateOf(
            Yaml.Default.encodeToString(
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
                    Yaml.Default.decodeFromString(pluginData.updaterSerializer, textField.text)
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


internal inline val Command.simpleDescription: AnnotatedString
    get() =
        with(AnnotatedString.Builder()) {
            append(allNames.joinToString { " " })
            append('\n')
            append(usage)
            append('\n')
            append(description)
            toAnnotatedString()
        }




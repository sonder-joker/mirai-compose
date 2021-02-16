package com.youngerhousea.miraicompose.ui.plugin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youngerhousea.miraicompose.theme.AppTheme
import net.mamoe.mirai.console.data.*
import net.mamoe.yamlkt.Yaml

private val yaml = Yaml.default

@Composable
fun PluginSettingView(pluginDatas: List<PluginData>) {
    LazyColumn(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(pluginDatas) { pluginData ->
            val kind = remember {
                when (pluginData) {
                    is AutoSavePluginData ->
                        "自动保存数据"
                    is AutoSavePluginConfig ->
                        "自动保存配置"
                    is ReadOnlyPluginData ->
                        "只读数据"
                    is ReadOnlyPluginConfig ->
                        "只读配置"
                    else -> "未知类型数据"
                }
            }
            Text(
                "$kind:${pluginData.saveName}", Modifier
                    .padding(bottom = 40.dp),
                fontSize = 20.sp
            )
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
                Text(value, textAlign = TextAlign.Center)
                TextField(textField, {
                    textField = it
                })
                Button(
                    {
                        kotlin.runCatching {
                            yaml.decodeFromString(pluginData.updaterSerializer, textField.text)
                        }.onSuccess {
                            value = textField.text
                        }.onFailure {
                            it.printStackTrace()
                        }
                    },
                    Modifier
                        .requiredWidth(100.dp)
                        .background(AppTheme.Colors.backgroundDark)
                ) {
                    Text("修改")
                }
            }
        }
    }
}
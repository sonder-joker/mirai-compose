package com.youngerhousea.miraicompose.ui.plugin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.mamoe.mirai.console.data.*
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.yamlkt.Yaml

private val yaml = Yaml.default

@OptIn(ConsoleExperimentalApi::class)
@Composable
fun PluginSettingView(pluginDatas: MutableList<PluginData>) {
    LazyColumn(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(pluginDatas) { pluginData ->
            Text(pluginData.saveName, Modifier.padding(bottom = 40.dp), fontSize = 20.sp)
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
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(value, textAlign = TextAlign.Center)
                TextField(textField, {
                    textField = it
                })
                FloatingActionButton({
                    kotlin.runCatching {
                        yaml.decodeFromString(pluginData.updaterSerializer, textField.text)
                    }.onSuccess {
                        value = textField.text
                    }.onFailure {
                        it.printStackTrace()
                    }
                }) { Text("修改") }
            }
        }
    }
}


//@OptIn(ConsoleExperimentalApi::class)
//@Composable
//private fun set(valueNodes: MutableList<AbstractPluginData.ValueNode<*>>) {
//    for (data in valueNodes) {
//        Row {
//            Text("${data.valueName}:")
//            when (val value = data.value) {
//                is PrimitiveValue<*> -> {
//                    when (value) {
//                        is IntValue -> {
//                            val v by value
//                            Text("$v")
//                        }
//                        is LongValue -> {
//                            var v by value
//                            Text("$v")
//                        }
//                        is ByteValue -> {
//                            var v by value
//                            Text("$v")
//                        }
//                        is DoubleValue -> {
//                            var v by value
//                            Text("$v")
//                        }
//                        is ShortValue -> {
//                            var v by value
//                            Text("$v")
//                        }
//                        is BooleanValue -> {
//                            var v by value
//                            Text("$v")
//                        }
//                        is StringValue -> {
//                            var v by value
//                            Text(v)
//                        }
//                        is CharValue -> {
//                            var v by value
//                            Text("$v")
//                        }
//                        is FloatValue -> {
//                            var v by value
//                            Text("$v")
//                        }
//                    }
//                }
//                is CompositeValue -> {
//                    when (value) {
//                        is SetValue<*> -> {
//                            val v by value
//                            Text(v.toString())
//                        }
//                        is MapValue<*, *> -> {
//                            val v by value
//                            Text(v.toString())
//                        }
//                        is ListValue<*> -> {
//                            val v by value
//                            Text(v.toString())
//                        }
//                    }
//                }
//            }
//        }
//    }
//}

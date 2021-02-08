package com.youngerhousea.miraicompose.ui.plugin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import net.mamoe.mirai.console.data.*
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

@OptIn(ConsoleExperimentalApi::class)
@Composable
fun PluginSettingView(pluginDatas: MutableList<AbstractPluginData>) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(pluginDatas) { pluginData ->
            set(pluginData.valueNodes)
            Spacer(Modifier.fillMaxWidth().height(10.dp).background(color = Color.Yellow))
        }
    }
}

@OptIn(ConsoleExperimentalApi::class)
@Composable
private fun set(valueNodes: MutableList<AbstractPluginData.ValueNode<*>>) {
    for (data in valueNodes) {
        Row {
            Text("${data.valueName}:")
            when (val value = data.value) {
                is PrimitiveValue<*> -> {
                    when (value) {
                        is IntValue -> {
                            val v by value
                            Text("$v")
                        }
                        is LongValue -> {
                            var v by value
                            Text("$v")
                        }
                        is ByteValue -> {
                            var v by value
                            Text("$v")
                        }
                        is DoubleValue -> {
                            var v by value
                            Text("$v")
                        }
                        is ShortValue -> {
                            var v by value
                            Text("$v")
                        }
                        is BooleanValue -> {
                            var v by value
                            Text("$v")
                        }
                        is StringValue -> {
                            var v by value
                            Text(v)
                        }
                        is CharValue -> {
                            var v by value
                            Text("$v")
                        }
                        is FloatValue -> {
                            var v by value
                            Text("$v")
                        }
                    }
                }
                is CompositeValue -> {
                    when (value) {
                        is SetValue<*> -> {
                            val v by value
                            Text(v.toString())
                        }
                        is MapValue<*, *> -> {
                            val v by value
                            Text(v.toString())
                        }
                        is ListValue<*> -> {
                            val v by value
                            Text(v.toString())
                        }
                    }
                }
            }
        }
    }
}
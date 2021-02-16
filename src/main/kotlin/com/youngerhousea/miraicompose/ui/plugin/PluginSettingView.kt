package com.youngerhousea.miraicompose.ui.plugin

import androidx.compose.runtime.Composable
import net.mamoe.mirai.console.data.AbstractPluginData
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

@OptIn(ConsoleExperimentalApi::class)
@Composable
fun PluginSettingView(pluginData: MutableList<AbstractPluginData>) {
    pluginData.forEach{
        it.valueNodes.forEach { node ->
               node.updaterSerializer
        }

    }
}
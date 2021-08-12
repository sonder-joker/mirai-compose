package com.youngerhousea.mirai.compose.ui.plugins

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.PluginManager

@Composable
fun Plugins() {
    var pluginsRoute by rememberSaveable { mutableStateOf<PluginsRoute>(PluginsRoute.List) }

    when(pluginsRoute) {
        PluginsRoute.List -> PluginList(PluginManager.plugins) {
            pluginsRoute = PluginsRoute.Single(it)
        }
        is PluginsRoute.Single -> {

        }
    }
}


sealed class PluginsRoute {
    object List : PluginsRoute()
    class Single(plugin:Plugin) : PluginsRoute()
}
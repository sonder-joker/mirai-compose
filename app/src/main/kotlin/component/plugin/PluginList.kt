package com.youngerhousea.miraicompose.component.plugin

import androidx.compose.material.SnackbarHostState
import net.mamoe.mirai.console.plugin.Plugin

/**
 * 插件列表
 */
interface PluginList {
    val onPluginCardClick: (plugin: Plugin) -> Unit

    val plugins: List<Plugin>

    val snackbarHostState: SnackbarHostState
}
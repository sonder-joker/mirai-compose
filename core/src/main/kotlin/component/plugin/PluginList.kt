package com.youngerhousea.miraicompose.core.component.plugin

import net.mamoe.mirai.console.plugin.Plugin

/**
 * 插件列表
 */
interface PluginList {
    val onPluginCardClick: (plugin: Plugin) -> Unit

    val plugins: List<Plugin>
}
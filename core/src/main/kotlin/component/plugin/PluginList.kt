package com.youngerhousea.miraicompose.core.component.plugin

import net.mamoe.mirai.console.plugin.Plugin

/**
 * 插件列表
 */
interface PluginList {

    val plugins: List<Plugin>

    fun onPluginCardClick(plugin: Plugin)
}
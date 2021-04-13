package com.youngerhousea.miraicompose.console

import com.youngerhousea.miraicompose.model.ComposeBot
import net.mamoe.mirai.console.data.PluginConfig
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin

interface MiraiConsoleRepository : AccessibleHolder {

    val composeBotList: MutableList<ComposeBot>

    val isReady: Boolean

    val loadedPlugins: List<Plugin>

}

interface AccessibleHolder {

    val JvmPlugin.data: List<PluginData>

    val JvmPlugin.config: List<PluginConfig>
}
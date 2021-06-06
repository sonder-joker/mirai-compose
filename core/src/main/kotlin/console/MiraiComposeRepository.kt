package com.youngerhousea.miraicompose.core.console

import net.mamoe.mirai.console.data.PluginConfig
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin

// 仅暴露必要的数据
interface MiraiComposeRepository : AccessibleHolder {

    // 已加载的bot
    val botList: MutableList<ComposeBot>


    // 已加载的插件
    val loadedPlugins: List<Plugin>
}

// 提供访问loadedJvmPlugin的data和config
interface AccessibleHolder {

    val JvmPlugin.data: List<PluginData>

    val JvmPlugin.config: List<PluginConfig>
}
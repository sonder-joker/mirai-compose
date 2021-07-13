package com.youngerhousea.miraicompose.core.console

import net.mamoe.mirai.console.data.PluginConfig
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin

// 提供访问loadedJvmPlugin的data和config
interface AccessibleHolder {

    val JvmPlugin.data: List<PluginData>

    val JvmPlugin.config: List<PluginConfig>
}
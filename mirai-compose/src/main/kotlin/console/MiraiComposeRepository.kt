package com.youngerhousea.miraicompose.console

import androidx.compose.ui.text.AnnotatedString
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.data.PluginConfig
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin

interface MiraiComposeRepository : AccessibleHolder {

    val botList: List<Bot?>

    val already: Boolean

    val loadedPlugins: List<Plugin>

    val annotatedLogStorage: List<AnnotatedString>

    fun addBot(bot: Bot?)
}

interface AccessibleHolder {

    val JvmPlugin.data: List<PluginData>

    val JvmPlugin.config: List<PluginConfig>
}
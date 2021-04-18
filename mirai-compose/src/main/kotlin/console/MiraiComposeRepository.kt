package com.youngerhousea.miraicompose.console

import androidx.compose.ui.text.AnnotatedString
import com.youngerhousea.miraicompose.model.ComposeBot
import net.mamoe.mirai.console.data.PluginConfig
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin

interface MiraiComposeRepository : AccessibleHolder {

    val composeBotList: List<ComposeBot>

    val already: Boolean

    val loadedPlugins: List<Plugin>

    val annotatedLogStorage: List<AnnotatedString>

    fun addBot(bot: ComposeBot)
}

interface AccessibleHolder {

    val JvmPlugin.data: List<PluginData>

    val JvmPlugin.config: List<PluginConfig>
}
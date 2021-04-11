package com.youngerhousea.miraicompose.console

import com.youngerhousea.miraicompose.model.ComposeBot
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin

interface MiraiConsoleRepository {
    val composeBotList: MutableList<ComposeBot>

    val isReady:Boolean

    val jvmPluginList: List<JvmPlugin>

    fun getConfig(plugin: Plugin):List<PluginData>

    fun getData(plugin: Plugin): List<PluginData>

    fun getDataWithConfig(plugin: Plugin) = getConfig(plugin) + getData(plugin)
}


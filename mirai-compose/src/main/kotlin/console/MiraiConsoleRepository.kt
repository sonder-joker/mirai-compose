package com.youngerhousea.miraicompose.console

import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin

interface MiraiConsoleRepository {

    val isReady:Boolean

    val jvmPluginList: List<JvmPlugin>

    fun getConfig(plugin: Plugin):List<PluginData>

    fun getData(plugin: Plugin): List<PluginData>

    fun getDataWithConfig(plugin: Plugin) = getConfig(plugin) + getData(plugin)
}


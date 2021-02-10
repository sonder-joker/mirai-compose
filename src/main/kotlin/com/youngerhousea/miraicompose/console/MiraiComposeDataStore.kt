package com.youngerhousea.miraicompose.console

import net.mamoe.mirai.console.data.MultiFilePluginDataStorage
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.data.PluginDataHolder
import net.mamoe.mirai.console.data.PluginDataStorage
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

@OptIn(ConsoleExperimentalApi::class)
class MiraiComposeDataStorageForJvmPluginLoader(
    private val delegete: PluginDataStorage
) : PluginDataStorage {
    val data = mutableMapOf<PluginDataHolder, MutableList<PluginData>>()

    @ConsoleExperimentalApi
    override fun load(holder: PluginDataHolder, instance: PluginData) {
        delegete.load(holder, instance)
        data.getOrPut(holder, ::mutableListOf).add(instance)
    }

    @ConsoleExperimentalApi
    override fun store(holder: PluginDataHolder, instance: PluginData) {
        delegete.store(holder, instance)
    }
}

@OptIn(ConsoleExperimentalApi::class)
fun MultiFilePluginDataStorage.toMiraiCompose(): MiraiComposeDataStorageForJvmPluginLoader =
    MiraiComposeDataStorageForJvmPluginLoader(this)


@OptIn(ConsoleExperimentalApi::class)
fun Plugin.getPluginData(): MutableList<PluginData> =
    MiraiCompose.dataStorageForJvmPluginLoader.data.getOrDefault(this as PluginDataHolder, mutableListOf())

@OptIn(ConsoleExperimentalApi::class)
fun  Plugin.getAllConfig()=
    MiraiCompose.configStorageForJvmPluginLoader.data.getOrDefault(this as PluginDataHolder, mutableListOf())

@OptIn(ConsoleExperimentalApi::class)
fun Plugin.getPluginConfig() =
    MiraiCompose.configStorageForJvmPluginLoader.data.getOrDefault(this as PluginDataHolder, mutableListOf())
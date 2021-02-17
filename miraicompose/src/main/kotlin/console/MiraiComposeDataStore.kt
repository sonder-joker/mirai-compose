@file:Suppress("TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")

package com.youngerhousea.miraicompose.console

import net.mamoe.mirai.console.data.MultiFilePluginDataStorage
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.data.PluginDataHolder
import net.mamoe.mirai.console.data.PluginDataStorage
import net.mamoe.mirai.console.plugin.Plugin

class MiraiComposeDataStorage(
    private val delegete: PluginDataStorage
) : PluginDataStorage {
    internal val data = mutableMapOf<PluginDataHolder, MutableList<PluginData>>()

    override fun load(holder: PluginDataHolder, instance: PluginData) {
        delegete.load(holder, instance)
        data.getOrPut(holder, ::mutableListOf).add(instance)
    }

    override fun store(holder: PluginDataHolder, instance: PluginData) {
        delegete.store(holder, instance)
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun MultiFilePluginDataStorage.toMiraiCompose(): MiraiComposeDataStorage =
    MiraiComposeDataStorage(this)


fun Plugin.getPluginData(): MutableList<PluginData> =
    MiraiCompose.dataStorageForJvmPluginLoader.data.getOrDefault(
        this,
        mutableListOf()
    )

fun Plugin.getPluginConfig() =
    MiraiCompose.configStorageForJvmPluginLoader.data.getOrDefault(this, mutableListOf())
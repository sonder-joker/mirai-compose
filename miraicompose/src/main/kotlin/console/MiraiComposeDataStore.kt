@file:Suppress("TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")

package com.youngerhousea.miraicompose.console

import net.mamoe.mirai.console.data.*
import net.mamoe.mirai.console.plugin.Plugin
import java.nio.file.Path

interface ReadablePluginDataStorage : PluginDataStorage {
    val data: MutableMap<PluginDataHolder, MutableList<PluginData>>

    companion object {
        /**
         * 创建一个 [ReadablePluginDataStorage] 实例，实现为[MultiFilePluginDataStorage].
         */
        operator fun invoke(directory: Path): ReadablePluginDataStorage =
            ReadablePluginDataStorageImpl(MultiFilePluginDataStorage(directory))
    }
}

private class ReadablePluginDataStorageImpl(
    private val delegete: PluginDataStorage
) : PluginDataStorage, ReadablePluginDataStorage {
    override val data = mutableMapOf<PluginDataHolder, MutableList<PluginData>>()

    override fun load(holder: PluginDataHolder, instance: PluginData) {
        delegete.load(holder, instance)
        data.getOrPut(holder, ::mutableListOf).add(instance)
    }

    override fun store(holder: PluginDataHolder, instance: PluginData) {
        delegete.store(holder, instance)
    }
}

fun Plugin.getPluginData(): MutableList<PluginData> =
    MiraiCompose.dataStorageForJvmPluginLoader.data.getOrDefault(
        this,
        mutableListOf()
    )

fun Plugin.getPluginConfig() =
    MiraiCompose.configStorageForJvmPluginLoader.data.getOrDefault(this, mutableListOf())
@file:Suppress("TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")

package com.youngerhousea.miraicompose.console

import net.mamoe.mirai.console.data.*
import net.mamoe.mirai.console.plugin.Plugin
import java.nio.file.Path

interface ReadablePluginDataStorage : PluginDataStorage {
    val dataMap: MutableMap<PluginDataHolder, MutableList<PluginData>>

    companion object {
        /**
         * 创建一个 [ReadablePluginDataStorage] 实例，实现为[MultiFilePluginDataStorage].
         */
        operator fun invoke(directory: Path): ReadablePluginDataStorage =
            ReadablePluginDataStorageImpl(MultiFilePluginDataStorage(directory))
    }

}

@Suppress("NOTHING_TO_INLINE")
inline operator fun ReadablePluginDataStorage.get(pluginDataHolder: PluginDataHolder): MutableList<PluginData> =
    this.dataMap[pluginDataHolder] ?: error("No Data!")

@Suppress("NOTHING_TO_INLINE")
inline fun Plugin.getPluginData(): MutableList<PluginData> =
    if (this is PluginDataHolder) MiraiCompose.dataStorageForJvmPluginLoader[this] else error("")

@Suppress("NOTHING_TO_INLINE")
inline fun Plugin.getPluginConfig(): MutableList<PluginData> =
    if (this is PluginDataHolder) MiraiCompose.configStorageForJvmPluginLoader[this] else error("")



private class ReadablePluginDataStorageImpl(
    private val delegete: PluginDataStorage
) : PluginDataStorage, ReadablePluginDataStorage {
    override val dataMap = mutableMapOf<PluginDataHolder, MutableList<PluginData>>()

    override fun load(holder: PluginDataHolder, instance: PluginData) {
        delegete.load(holder, instance)
        dataMap.getOrPut(holder, ::mutableListOf).add(instance)
    }

    override fun store(holder: PluginDataHolder, instance: PluginData) {
        delegete.store(holder, instance)
    }
}

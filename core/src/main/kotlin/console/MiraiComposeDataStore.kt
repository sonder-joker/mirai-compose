package com.youngerhousea.miraicompose.core.console

import net.mamoe.mirai.console.data.MultiFilePluginDataStorage
import net.mamoe.mirai.console.data.PluginConfig
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.data.PluginDataHolder
import java.nio.file.Path

/**
 * 可读的数据存储,由[MultiFilePluginDataStorage]代理实现
 *
 * @see ReadablePluginConfigStorage
 */
interface ReadablePluginDataStorage : MultiFilePluginDataStorage {
    val dataMap: Map<PluginDataHolder, MutableList<PluginData>>

    operator fun get(pluginDataHolder: PluginDataHolder): List<PluginData> =
        this.dataMap[pluginDataHolder] ?: emptyList()

    companion object {
        /**
         * 创建一个 [ReadablePluginDataStorage] 实例.
         */
        operator fun invoke(directory: Path): ReadablePluginDataStorage =
            ReadablePluginDataStorageImpl(MultiFilePluginDataStorage(directory))
    }
}

private class ReadablePluginDataStorageImpl(
    private val storage: MultiFilePluginDataStorage
) : MultiFilePluginDataStorage by storage, ReadablePluginDataStorage {
    override val dataMap = mutableMapOf<PluginDataHolder, MutableList<PluginData>>()

    override fun load(holder: PluginDataHolder, instance: PluginData) {
        storage.load(holder, instance)
        dataMap.getOrPut(holder, ::mutableListOf).add(instance)
    }

    override fun store(holder: PluginDataHolder, instance: PluginData) {
        storage.store(holder, instance)
    }
}


/**
 * 可读的配置存储，由[MultiFilePluginDataStorage]代理实现
 *
 * @see ReadablePluginDataStorage
 */
interface ReadablePluginConfigStorage : MultiFilePluginDataStorage {
    val dataMap: Map<PluginDataHolder, MutableList<PluginConfig>>

    operator fun get(pluginDataHolder: PluginDataHolder): List<PluginConfig> =
        this.dataMap[pluginDataHolder] ?: emptyList()

    companion object {
        /**
         * 创建一个 [ReadablePluginConfigStorage] 实例，实现为[MultiFilePluginDataStorage].
         */
        operator fun invoke(directory: Path): ReadablePluginConfigStorage =
            ReadablePluginConfigStorageImpl(MultiFilePluginDataStorage(directory))
    }
}

private class ReadablePluginConfigStorageImpl(
    private val storage: MultiFilePluginDataStorage
) : MultiFilePluginDataStorage by storage, ReadablePluginConfigStorage {
    override val dataMap = mutableMapOf<PluginDataHolder, MutableList<PluginConfig>>()

    override fun load(holder: PluginDataHolder, instance: PluginData) {
        storage.load(holder, instance)
        dataMap.getOrPut(holder, ::mutableListOf).add(instance as PluginConfig)
    }

    override fun store(holder: PluginDataHolder, instance: PluginData) {
        storage.store(holder, instance as PluginConfig)
    }
}





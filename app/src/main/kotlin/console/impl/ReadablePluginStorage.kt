package com.youngerhousea.mirai.compose.console.impl

import net.mamoe.mirai.console.data.MultiFilePluginDataStorage
import net.mamoe.mirai.console.data.PluginConfig
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.data.PluginDataHolder
import java.nio.file.Path

/**
 * 可读的数据存储,由[MultiFilePluginDataStorage]代理实现
 */
interface ReadablePluginDataStorage : MultiFilePluginDataStorage {
    val dataMap: Map<PluginDataHolder, MutableList<PluginData>>
}

operator fun ReadablePluginDataStorage.get(pluginDataHolder: PluginDataHolder): List<PluginData> =
    dataMap[pluginDataHolder] ?: emptyList()

/**
 * 创建一个 [ReadablePluginDataStorage] 实例，实现为[MultiFilePluginDataStorage].
 */
fun ReadablePluginDataStorage(directory: Path): ReadablePluginDataStorage =
    ReadablePluginDataStorageImpl(MultiFilePluginDataStorage(directory))



/**
 * 可读的配置存储，由[MultiFilePluginDataStorage]代理实现
 */
interface ReadablePluginConfigStorage : MultiFilePluginDataStorage {
    val dataMap: Map<PluginDataHolder, MutableList<PluginConfig>>
}

operator fun ReadablePluginConfigStorage.get(pluginDataHolder: PluginDataHolder): List<PluginConfig> =
    dataMap[pluginDataHolder] ?: emptyList()

/**
 * 创建一个 [ReadablePluginConfigStorage] 实例，实现为[MultiFilePluginDataStorage].
 */
fun ReadablePluginConfigStorage(directory: Path): ReadablePluginConfigStorage =
    ReadablePluginConfigStorageImpl(MultiFilePluginDataStorage(directory))


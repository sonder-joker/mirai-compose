package com.youngerhousea.mirai.compose.console.impl

import net.mamoe.mirai.console.data.MultiFilePluginDataStorage
import net.mamoe.mirai.console.data.PluginConfig
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.data.PluginDataHolder


internal class ReadablePluginDataStorageImpl(
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

internal class ReadablePluginConfigStorageImpl(
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
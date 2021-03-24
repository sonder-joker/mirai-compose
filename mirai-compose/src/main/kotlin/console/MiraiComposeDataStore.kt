@file:Suppress("TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")

package com.youngerhousea.miraicompose.console

import com.youngerhousea.miraicompose.theme.ComposeSetting
import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.data.*
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.util.CoroutineScopeUtils.childScope
import java.nio.file.Path

interface ReadablePluginDataStorage : MultiFilePluginDataStorage {
    val dataMap: MutableMap<PluginDataHolder, MutableList<PluginData>>

    operator fun get(pluginDataHolder: PluginDataHolder): List<PluginData> =
        this.dataMap[pluginDataHolder] ?: emptyList()

    companion object {
        /**
         * 创建一个 [ReadablePluginDataStorage] 实例，实现为[MultiFilePluginDataStorage].
         */
        operator fun invoke(directory: Path): ReadablePluginDataStorage =
            ReadablePluginDataStorageImpl(MultiFilePluginDataStorage(directory))

    }
}

private class ReadablePluginDataStorageImpl(
    private val delegete: MultiFilePluginDataStorage
) : MultiFilePluginDataStorage by delegete, ReadablePluginDataStorage {
    override val dataMap = mutableMapOf<PluginDataHolder, MutableList<PluginData>>()

    override fun load(holder: PluginDataHolder, instance: PluginData) {
        delegete.load(holder, instance)
        dataMap.getOrPut(holder, ::mutableListOf).add(instance)
    }

    override fun store(holder: PluginDataHolder, instance: PluginData) {
        delegete.store(holder, instance)
    }
}

val ConfigStorageForCompose = ReadablePluginDataStorage(MiraiConsole.rootPath.resolve("config"))

internal object ComposeDataScope : CoroutineScope by MiraiConsole.childScope("ComposeDataScope") {
    private val configs: MutableList<PluginConfig> = mutableListOf(ComposeSetting)

    fun addAndReloadConfig(config: PluginConfig) {
        configs.add(config)
        ConfigStorageForCompose.load(ComposeBuiltInConfigHolder, config)
    }

    fun reloadAll() {
        configs.forEach { config ->
            ConfigStorageForCompose.load(ComposeBuiltInConfigHolder, config)
        }
    }
}

internal object ComposeBuiltInConfigHolder : AutoSavePluginDataHolder,
    CoroutineScope by ComposeDataScope.childScope("ComposeBuiltInPluginDataHolder") {
    override val autoSaveIntervalMillis: LongRange = 1 * (60 * 1000L)..10 * (60 * 1000L)
    override val dataHolderName: String = "Compose"
}





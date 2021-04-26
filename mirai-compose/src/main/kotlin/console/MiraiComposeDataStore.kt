package com.youngerhousea.miraicompose.console

import com.youngerhousea.miraicompose.theme.ComposeSetting
import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.data.*
import net.mamoe.mirai.console.util.CoroutineScopeUtils.childScope
import java.nio.file.Path

/**
 * 可读的数据存储,由[MultiFilePluginDataStorage]代理实现
 *
 * @see ReadablePluginConfigStorage
 */
interface ReadablePluginDataStorage : MultiFilePluginDataStorage {
    val dataMap: MutableMap<PluginDataHolder, MutableList<PluginData>>

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


/**
 * 可读的配置存储，由[MultiFilePluginDataStorage]代理实现
 *
 * @see ReadablePluginDataStorage
 */
interface ReadablePluginConfigStorage : MultiFilePluginDataStorage {
    val dataMap: MutableMap<PluginDataHolder, MutableList<PluginConfig>>

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
    private val delegete: MultiFilePluginDataStorage
) : MultiFilePluginDataStorage by delegete, ReadablePluginConfigStorage {
    override val dataMap = mutableMapOf<PluginDataHolder, MutableList<PluginConfig>>()

    override fun load(holder: PluginDataHolder, instance: PluginData) {
        delegete.load(holder, instance)
        dataMap.getOrPut(holder, ::mutableListOf).add(instance as PluginConfig)
    }

    override fun store(holder: PluginDataHolder, instance: PluginData) {
        delegete.store(holder, instance as PluginConfig)
    }
}

/**
 * [MiraiCompose]的DataScope，用于存储配置
 */
internal object ComposeDataScope : CoroutineScope by MiraiConsole.childScope("ComposeDataScope") {
    private val configStorageForCompose = ReadablePluginDataStorage(MiraiConsole.rootPath.resolve("config"))

    private val configs: MutableList<PluginConfig> = mutableListOf(ComposeSetting)

    fun addAndReloadConfig(config: PluginConfig) {
        configs.add(config)
        configStorageForCompose.load(ComposeBuiltInConfigHolder, config)
    }

    fun reloadAll() {
        configs.forEach { config ->
            configStorageForCompose.load(ComposeBuiltInConfigHolder, config)
        }
    }
}

/**
 * [MiraiCompose]的内置ConfigHolder，用于存储配置
 */
private object ComposeBuiltInConfigHolder : AutoSavePluginDataHolder,
    CoroutineScope by ComposeDataScope.childScope("ComposeBuiltInPluginDataHolder") {
    override val autoSaveIntervalMillis: LongRange = 1 * (60_1000L)..10 * (60_1000L)
    override val dataHolderName: String = "Compose"
}





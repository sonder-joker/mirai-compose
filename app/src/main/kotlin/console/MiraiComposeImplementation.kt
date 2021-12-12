package com.youngerhousea.mirai.compose.console

import com.youngerhousea.mirai.compose.console.impl.LifecycleOwner
import com.youngerhousea.mirai.compose.console.impl.Log
import com.youngerhousea.mirai.compose.console.impl.ReadablePluginConfigStorage
import com.youngerhousea.mirai.compose.console.impl.ReadablePluginDataStorage
import kotlinx.coroutines.flow.StateFlow
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.data.PluginConfig
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin

interface MiraiComposeImplementation :
    MiraiConsoleImplementation,
    ViewModelStoreOwner,
    LifecycleOwner {
    override val configStorageForBuiltIns: ReadablePluginConfigStorage

    override val configStorageForJvmPluginLoader: ReadablePluginConfigStorage

    override val dataStorageForBuiltIns: ReadablePluginDataStorage

    override val dataStorageForJvmPluginLoader: ReadablePluginDataStorage

    val JvmPlugin.data: List<PluginData>

    val JvmPlugin.config: List<PluginConfig>

    val logStorage: StateFlow<List<Log>>

    fun cancel()
}


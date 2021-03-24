package com.youngerhousea.miraicompose.console

import androidx.compose.runtime.*
import com.youngerhousea.miraicompose.console.*
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.utils.setSystemOut
import kotlinx.coroutines.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.ConsoleFrontEndImplementation
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.MiraiConsoleFrontEndDescription
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.data.PluginConfig
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.data.PluginDataHolder
import net.mamoe.mirai.console.extensions.BotConfigurationAlterer
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.PluginManager
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.JvmPluginLoader
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleInput
import net.mamoe.mirai.console.util.NamedSupervisorJob
import net.mamoe.mirai.console.util.SemVersion
import net.mamoe.mirai.utils.*
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*


@ConsoleFrontEndImplementation
class MiraiCompose : MiraiConsoleImplementation, MiraiConsoleRepository, CoroutineScope by CoroutineScope(
    NamedSupervisorJob("MiraiCompose") + CoroutineExceptionHandler { coroutineContext, throwable ->
        if (throwable is CancellationException) {
            return@CoroutineExceptionHandler
        }
        val coroutineName = coroutineContext[CoroutineName]?.name ?: "<unnamed>"
        MiraiConsole.mainLogger.error("Exception in coroutine $coroutineName", throwable)
    }
) {
    override val rootPath: Path =
        Paths.get(System.getProperty("user.dir", ".")).toAbsolutePath()

    override val builtInPluginLoaders =
        listOf(lazy { JvmPluginLoader })

    override val frontEndDescription =
        MiraiComposeDescription

    override val dataStorageForJvmPluginLoader = ReadablePluginDataStorage(rootPath.resolve("data"))

    override val dataStorageForBuiltIns = ReadablePluginDataStorage(rootPath.resolve("data"))

    override val configStorageForJvmPluginLoader = ReadablePluginDataStorage(rootPath.resolve("config"))

    override val configStorageForBuiltIns = ReadablePluginDataStorage(rootPath.resolve("config"))

    override val consoleInput: ConsoleInput =
        MiraiComposeInput

    override val consoleCommandSender =
        MiraiComposeSender

    override fun createLogger(identity: String?): MiraiLogger =
        MiraiComposeLogger(identity)

    override fun createLoginSolver(requesterBot: Long, configuration: BotConfiguration) =
        SwingSolver

    override var isReady by mutableStateOf(false)

    @Suppress("UNCHECKED_CAST")
    override val jvmPluginList: List<JvmPlugin> by lazy {  PluginManager.plugins as List<JvmPlugin> }

    @Suppress("UNCHECKED_CAST")
    override fun getConfig(plugin: Plugin): List<PluginConfig> =
        if (plugin is PluginDataHolder) configStorageForJvmPluginLoader[plugin] as List<PluginConfig> else error("Plugin is Not Holder!")

    override fun getData(plugin: Plugin): List<PluginData> =
        if (plugin is PluginDataHolder) dataStorageForJvmPluginLoader[plugin] else error("Plugin is Not Holder!")

    override fun preStart() {
        if (!DEBUG)
            setSystemOut(MiraiComposeLogger.out)
    }

    override fun prePhase(phase: String) {
        if (phase == "auto-login bots") {
            backendAccess.globalComponentStorage.contribute(BotConfigurationAlterer, InternalHook, InternalHook)
        }
    }

    override fun postPhase(phase: String) {
        if (phase == "auto-login bots") {
            Bot.instances.forEach {
                ComposeBot.instances.add(ComposeBot(it))
            }
        }
        if (phase == "load configurations") {
            ComposeDataScope.reloadAll()
        }
    }

    override fun postStart() {
        isReady = true
    }

}

const val DEBUG = true


object MiraiComposeDescription : MiraiConsoleFrontEndDescription {
    override val name: String = "MiraiCompose"
    override val vendor: String = "Noire"
    override val version: SemVersion = SemVersion("1.1.0")
}


internal object InternalHook :
    KotlinPlugin(JvmPluginDescription("com.youngerhousea.internal", MiraiComposeDescription.version)),
    BotConfigurationAlterer {
    override fun alterConfiguration(botId: Long, configuration: BotConfiguration): BotConfiguration {
        println("114514")
        return configuration
    }
}


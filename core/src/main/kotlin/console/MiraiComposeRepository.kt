package com.youngerhousea.miraicompose.core.console

import com.arkivanov.decompose.instancekeeper.InstanceKeeper
import com.youngerhousea.miraicompose.core.configPath
import com.youngerhousea.miraicompose.core.data.BotItem
import com.youngerhousea.miraicompose.core.data.BotItemImpl
import com.youngerhousea.miraicompose.core.dataPath
import com.youngerhousea.miraicompose.core.root
import kotlinx.coroutines.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.data.PluginConfig
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.data.PluginDataHolder
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin
import net.mamoe.mirai.console.plugin.jvm.JvmPluginLoader
import net.mamoe.mirai.console.util.ConsoleInput
import net.mamoe.mirai.console.util.NamedSupervisorJob
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.LoginSolver
import net.mamoe.mirai.utils.MiraiLogger
import java.nio.file.Path

// 提供访问loadedJvmPlugin的data和config
interface AccessibleHolder {

    val JvmPlugin.data: List<PluginData>

    val JvmPlugin.config: List<PluginConfig>
}

internal class MiraiComposeViewModel(
    val onLoggerLog: (Log) -> Unit,
    val onAutoLogin: (List<BotItem>) -> Unit,
    val createLoginSolver:() -> LoginSolver,
    override val rootPath: Path = root
) : MiraiConsoleImplementation, AccessibleHolder, InstanceKeeper.Instance, CoroutineScope by CoroutineScope(
    NamedSupervisorJob("MiraiCompose") + CoroutineExceptionHandler { coroutineContext, throwable ->
        if (throwable is CancellationException) {
            return@CoroutineExceptionHandler
        }
        val coroutineName = coroutineContext[CoroutineName]?.name ?: "<unnamed>"
        MiraiConsole.mainLogger.error("Exception in coroutine $coroutineName", throwable)
    }
) {
    override val builtInPluginLoaders = listOf(lazy { JvmPluginLoader })

    override val frontEndDescription = MiraiComposeDescription

    override val dataStorageForJvmPluginLoader = ReadablePluginDataStorage(dataPath)

    override val dataStorageForBuiltIns = ReadablePluginDataStorage(dataPath)

    override val configStorageForJvmPluginLoader = ReadablePluginConfigStorage(configPath)

    override val configStorageForBuiltIns = ReadablePluginConfigStorage(configPath)

    override val consoleInput: ConsoleInput = MiraiComposeInput

    override val consoleCommandSender = object : MiraiConsoleImplementation.ConsoleCommandSenderImpl {
        override suspend fun sendMessage(message: String) {
            logger.info(message)
        }

        override suspend fun sendMessage(message: Message) {
            sendMessage(message.toString())
        }
    }


    val logger by lazy { createLogger("compose") }

    override fun createLogger(identity: String?): MiraiLogger =
        MiraiComposeLogger(identity) { message: String?, throwable: Throwable?, priority: LogPriority ->
            val log = Log(priority, "${identity ?: "Default"}:$message", throwable, identity)
            println(
                "${
                    when (priority) {
                        LogPriority.VERBOSE -> ANSI.GREEN
                        LogPriority.INFO -> ANSI.GREEN
                        LogPriority.WARNING -> ANSI.YELLOW
                        LogPriority.ERROR -> ANSI.RED
                        LogPriority.DEBUG -> ANSI.PURPLE
                    }.value
                }${log.compositionLog}${ANSI.RESET.value}"
            )
            onLoggerLog(log)
        }

    override fun createLoginSolver(requesterBot: Long, configuration: BotConfiguration): LoginSolver = createLoginSolver()

    override val JvmPlugin.data: List<PluginData>
        get() = if (this is PluginDataHolder) dataStorageForJvmPluginLoader[this] else error("Plugin is Not Holder!")

    override val JvmPlugin.config: List<PluginConfig>
        get() = if (this is PluginDataHolder) configStorageForJvmPluginLoader[this] else error("Plugin is Not Holder!")

    override fun postPhase(phase: String) {
        when (phase) {
            "auto-login bots" ->
                onAutoLogin(Bot.instances.map { BotItemImpl(it) })

//            "load configurations" ->
//                ComposeDataScope.reloadAll()
//            "setup logger controller" -> {
//                assert(loggerController === ComposeLoggerController) { "?" }
//                ComposeDataScope.addAndReloadConfig(LoggerConfig)
//                ComposeLoggerController.initialized = true
//            }
        }
    }

    override fun onDestroy() {
        cancel()
    }

}
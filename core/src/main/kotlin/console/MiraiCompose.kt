package com.youngerhousea.miraicompose.core.console

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.MiraiConsoleFrontEndDescription
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.data.MultiFilePluginDataStorage
import net.mamoe.mirai.console.data.PluginConfig
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.data.PluginDataHolder
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin
import net.mamoe.mirai.console.plugin.jvm.JvmPluginLoader
import net.mamoe.mirai.console.util.ConsoleInput
import net.mamoe.mirai.console.util.NamedSupervisorJob
import net.mamoe.mirai.console.util.SemVersion
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.LoginSolver
import net.mamoe.mirai.utils.MiraiLogger
import net.mamoe.mirai.utils.SwingSolver
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.div

/**
 * MiraiCompose 的实现
 *
 */
class MiraiCompose(
    val loginSolver: (requesterBot: Long, configuration: BotConfiguration) -> LoginSolver
) : MiraiConsoleImplementation, AccessibleHolder,
    CoroutineScope by CoroutineScope(
        NamedSupervisorJob("MiraiCompose") + CoroutineExceptionHandler { coroutineContext, throwable ->
            if (throwable is CancellationException) {
                return@CoroutineExceptionHandler
            }
            val coroutineName = coroutineContext[CoroutineName]?.name ?: "<unnamed>"
            MiraiConsole.mainLogger.error("Exception in coroutine $coroutineName", throwable)
        }
    ) {

    override val rootPath: Path = Paths.get(System.getProperty("user.dir", ".")).toAbsolutePath()

    override val builtInPluginLoaders = listOf(lazy { JvmPluginLoader })

    override val frontEndDescription = MiraiComposeDescription

    override val dataStorageForJvmPluginLoader = ReadablePluginDataStorage(rootPath / "data")

    override val dataStorageForBuiltIns = MultiFilePluginDataStorage(rootPath / "data")

    override val configStorageForJvmPluginLoader = ReadablePluginConfigStorage(rootPath / "config")

    override val configStorageForBuiltIns = MultiFilePluginDataStorage(rootPath / "config")

    override val consoleInput: ConsoleInput = MiraiComposeInput

    override val consoleCommandSender: MiraiComposeSender = MiraiComposeSender

    override fun createLogger(identity: String?): MiraiLogger = MiraiComposeLogger(identity)

    // 一般不应该被使用
    override fun createLoginSolver(requesterBot: Long, configuration: BotConfiguration) =
        loginSolver(requesterBot, configuration)

    override val JvmPlugin.data: List<PluginData>
        get() = if (this is PluginDataHolder) dataStorageForJvmPluginLoader[this] else error("Plugin is Not Holder!")

    override val JvmPlugin.config: List<PluginConfig>
        get() = if (this is PluginDataHolder) configStorageForJvmPluginLoader[this] else error("Plugin is Not Holder!")


    override fun postPhase(phase: String) {
        when (phase) {
            "load configurations" ->
                ComposeDataScope.reloadAll()
        }
    }

    init {
        instance = this
    }

    companion object {
        lateinit var instance: MiraiCompose

        val logger by lazy { MiraiConsole.createLogger("compose") }
    }
}

object MiraiComposeDescription : MiraiConsoleFrontEndDescription {
    override val name: String = "MiraiCompose"
    override val vendor: String = "Noire"
    override val version: SemVersion = SemVersion("1.0.0")
}


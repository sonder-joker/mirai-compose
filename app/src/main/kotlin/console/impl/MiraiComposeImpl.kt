package com.youngerhousea.mirai.compose.console.impl

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.youngerhousea.mirai.compose.console.MiraiComposeImplementation
import com.youngerhousea.mirai.compose.console.MiraiComposeLoginSolver
import com.youngerhousea.mirai.compose.console.Solver
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.SupervisorJob
import mirai_compose.app.BuildConfig
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.MiraiConsoleFrontEndDescription
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.data.PluginConfig
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.data.PluginDataHolder
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin
import net.mamoe.mirai.console.plugin.jvm.JvmPluginLoader
import net.mamoe.mirai.console.util.ConsoleInput
import net.mamoe.mirai.console.util.SemVersion
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.LoginSolver
import net.mamoe.mirai.utils.MiraiInternalApi
import net.mamoe.mirai.utils.MiraiLogger
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.coroutines.CoroutineContext
import kotlin.io.path.div

/**
 * Create a [MiraiComposeImplementation], this implementation for [MiraiConsoleImplementation]
 *
 */
val MiraiCompose: MiraiComposeImplementation by lazy { MiraiComposeImpl() }

internal class MiraiComposeImpl : MiraiComposeImplementation, Solver by MiraiComposeLoginSolver {

    override val composeLogger: MiraiLogger by lazy { createLogger("compose") }

    override val rootPath: Path = Paths.get(System.getProperty("user.dir", ".")).toAbsolutePath()

    override val builtInPluginLoaders = listOf(lazy { JvmPluginLoader })

    override val commandManager: CommandManager by lazy { backendAccess.createDefaultCommandManager(coroutineContext) }

    override val frontEndDescription = MiraiComposeDescription

    override val jvmPluginLoader: JvmPluginLoader by lazy { backendAccess.createDefaultJvmPluginLoader(coroutineContext) }

    override val dataStorageForJvmPluginLoader = ReadablePluginDataStorage(rootPath / "data")

    override val dataStorageForBuiltIns = ReadablePluginDataStorage(rootPath / "data")

    override val configStorageForJvmPluginLoader = ReadablePluginConfigStorage(rootPath / "config")

    override val configStorageForBuiltIns = ReadablePluginConfigStorage(rootPath / "config")

    override val consoleInput: ConsoleInput = MiraiComposeInput

    override val consoleCommandSender = MiraiConsoleSender(composeLogger)

    override val consoleDataScope: MiraiConsoleImplementation.ConsoleDataScope by lazy {
             MiraiConsoleImplementation.ConsoleDataScope.createDefault(
                 coroutineContext,
                 dataStorageForBuiltIns,
                 configStorageForBuiltIns
                    )
         }

    @OptIn(MiraiInternalApi::class)
    override fun createLogger(identity: String?): MiraiLogger =
        MiraiComposeLogger(identity = identity ?: "Default", output = { content, exception, priority ->
            val newContent =
                if (exception != null) (content ?: exception.toString()) + "\n${exception.stackTraceToString()}"
                else content.toString()
            val trueContent = "$currentTimeFormatted ${priority.simpleName}/$identity $newContent"

            printForDebug(priority, trueContent)
            storeInLogStorage(priority, trueContent)
        })

    private fun storeInLogStorage(priority: LogKind, content: String) {
        logStorage.value += Log(content, priority)
    }

    private fun printForDebug(priority: LogKind, content: String) {
        println("${priority.textColor}${content}${TextColor.RESET}")
    }

    override val JvmPlugin.data: List<PluginData>
        get() = if (this is PluginDataHolder) dataStorageForJvmPluginLoader[this] else error("Plugin is Not Holder!")

    override val JvmPlugin.config: List<PluginConfig>
        get() = if (this is PluginDataHolder) configStorageForJvmPluginLoader[this] else error("Plugin is Not Holder!")

    override val logStorage: MutableState<List<Log>> = mutableStateOf(listOf())

    override fun createLoginSolver(requesterBot: Long, configuration: BotConfiguration): LoginSolver = MiraiComposeLoginSolver

    override val coroutineContext: CoroutineContext =
        CoroutineName("MiraiCompose") + CoroutineExceptionHandler { coroutineContext, throwable ->
            if (throwable is CancellationException) {
                return@CoroutineExceptionHandler
            }
            val coroutineName = coroutineContext[CoroutineName]?.name ?: "<unnamed>"
            MiraiConsole.mainLogger.error("Exception in coroutine $coroutineName", throwable)
        } + SupervisorJob()

}

object MiraiComposeDescription : MiraiConsoleFrontEndDescription {
    override val name: String = BuildConfig.projectName
    override val vendor: String = BuildConfig.projectGroup
    override val version: SemVersion = SemVersion(BuildConfig.projectVersion)
}

object MiraiComposeInput : ConsoleInput {
    override suspend fun requestInput(hint: String): String {
        error("Not yet implemented")
    }
}


class MiraiConsoleSender(
    private val logger: MiraiLogger
) : MiraiConsoleImplementation.ConsoleCommandSenderImpl {
    override suspend fun sendMessage(message: String) {
        logger.info(message)
    }

    override suspend fun sendMessage(message: Message) {
        sendMessage(message.toString())
    }
}

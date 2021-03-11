package com.youngerhousea.miraicompose

import com.youngerhousea.miraicompose.console.*
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.ui.navigation.MiraiComposeView
import com.youngerhousea.miraicompose.utils.setSystemOut
import kotlinx.coroutines.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.ConsoleFrontEndImplementation
import net.mamoe.mirai.console.MiraiConsoleFrontEndDescription
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start
import net.mamoe.mirai.console.plugin.PluginManager
import net.mamoe.mirai.console.plugin.jvm.JvmPluginLoader
import net.mamoe.mirai.console.util.ConsoleInput
import net.mamoe.mirai.console.util.NamedSupervisorJob
import net.mamoe.mirai.console.util.SemVersion
import net.mamoe.mirai.utils.*
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*



@ConsoleFrontEndImplementation
object MiraiCompose : MiraiConsoleImplementation, CoroutineScope by CoroutineScope(
    NamedSupervisorJob("All") + CoroutineExceptionHandler { _, throwable ->
        if (throwable is CancellationException) {
            return@CoroutineExceptionHandler
        }
        PluginManager
        throwable.printStackTrace()
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


    override fun preStart() {
        setSystemOut(MiraiComposeLogger.out)
    }

    override fun postPhase(phase: String) {
        if (phase == "auto-login bots") {
            Bot.instances.forEach {
                ComposeBot(it)
            }
        }

        if (phase == "load configurations") {
            ComposeDataScope.reloadAll()
        }
    }
}



object MiraiComposeDescription : MiraiConsoleFrontEndDescription {
    override val name: String = "MiraiCompose"
    override val vendor: String = "Noire"
    override val version: SemVersion = SemVersion("1.1.0")
}


// Compose Entry Point
fun main() {
    MiraiComposeView()
    MiraiCompose.start()
}

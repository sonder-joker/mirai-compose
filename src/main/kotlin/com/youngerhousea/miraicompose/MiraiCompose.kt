package com.youngerhousea.miraicompose

import com.youngerhousea.miraicompose.console.MiraiComposeInput
import com.youngerhousea.miraicompose.console.MiraiComposeLogger
import com.youngerhousea.miraicompose.console.MiraiComposeSender
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.console.ConsoleFrontEndImplementation
import net.mamoe.mirai.console.MiraiConsoleFrontEndDescription
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.data.MultiFilePluginDataStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.console.util.ConsoleInput
import net.mamoe.mirai.console.util.NamedSupervisorJob
import net.mamoe.mirai.console.util.SemVersion
import net.mamoe.mirai.utils.*
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*


@OptIn(ConsoleExperimentalApi::class)
@ConsoleFrontEndImplementation
object MiraiCompose : MiraiConsoleImplementation, CoroutineScope by CoroutineScope(
    NamedSupervisorJob("All") + CoroutineExceptionHandler { _, throwable ->
        if (throwable is CancellationException) {
            return@CoroutineExceptionHandler
        }
        throwable.printStackTrace()
    }
) {
    override val rootPath: Path =
        Paths.get(System.getProperty("user.dir", ".")).toAbsolutePath()

    override val builtInPluginLoaders =
        listOf(lazy { JvmPluginLoader })

    override val frontEndDescription =
        ComposeDescription

    override val dataStorageForJvmPluginLoader =
        MultiFilePluginDataStorage(rootPath.resolve("data"))

    override val dataStorageForBuiltIns =
        MultiFilePluginDataStorage(rootPath.resolve("data"))

    override val configStorageForJvmPluginLoader =
        MultiFilePluginDataStorage(rootPath.resolve("config"))

    override val configStorageForBuiltIns =
        MultiFilePluginDataStorage(rootPath.resolve("config"))

    override val consoleInput: ConsoleInput =
        MiraiComposeInput

    override val consoleCommandSender =
        MiraiComposeSender

    override fun createLogger(identity: String?): MiraiLogger =
        MiraiComposeLogger

    @OptIn(MiraiExperimentalApi::class)
    override fun createLoginSolver(requesterBot: Long, configuration: BotConfiguration) =
        SwingSolver
}


object ComposeDescription : MiraiConsoleFrontEndDescription {
    override val name: String
        get() = "MiraiCompose"
    override val vendor: String
        get() = "Noire"
    override val version: SemVersion
        get() = SemVersion("0.1.0")
}

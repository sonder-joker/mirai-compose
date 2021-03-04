package com.youngerhousea.miraicompose.console

import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.theme.AppTheme
import com.youngerhousea.miraicompose.ui.MiraiComposeView
import kotlinx.coroutines.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.ConsoleFrontEndImplementation
import net.mamoe.mirai.console.MiraiConsoleFrontEndDescription
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.plugin.jvm.JvmPluginLoader
import net.mamoe.mirai.console.util.ConsoleInput
import net.mamoe.mirai.console.util.NamedSupervisorJob
import net.mamoe.mirai.console.util.SemVersion
import net.mamoe.mirai.utils.*
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.createDirectories

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

    //TODO: better create
    val composeFile = rootPath.resolve("compose").createDirectories()

    val logFiles = composeFile.resolve("log").createDirectories()

    val configFiles = composeFile.resolve("config").createDirectories()


    init {
        this.coroutineContext[Job]?.invokeOnCompletion {
            Files.write(configFiles.resolve("theme"), Json.encodeToString(AppTheme).toByteArray())
        }
    }

    override fun preStart() {
        setSystemOut(out)
        MiraiComposeView()
    }

    override fun postPhase(phase: String) {
        if (phase == "auto-login bots") {
            Bot.instances.forEach {
                ComposeBot(it)
            }
        }
    }
}

internal val MiraiCompose.out get() = MiraiLogger.create("stdout")

internal fun setSystemOut(out: MiraiLogger) {
    System.setOut(
        PrintStream(
            BufferedOutputStream(
                logger = out.run { ({ line: String? -> info(line) }) }
            ),
            false,
            "UTF-8"
        )
    )
    System.setErr(
        PrintStream(
            BufferedOutputStream(
                logger = out.run { ({ line: String? -> warning(line) }) }
            ),
            false,
            "UTF-8"
        )
    )
}

object MiraiComposeDescription : MiraiConsoleFrontEndDescription {
    override val name: String
        get() = "MiraiCompose"
    override val vendor: String
        get() = "Noire"
    override val version: SemVersion
        get() = SemVersion("1.1.0")
}


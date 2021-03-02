package com.youngerhousea.miraicompose.console

import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.model.Model
import com.youngerhousea.miraicompose.ui.MiraiComposeView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.ConsoleFrontEndImplementation
import net.mamoe.mirai.console.MiraiConsoleFrontEndDescription
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.MultiFilePluginDataStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginLoader
import net.mamoe.mirai.console.util.ConsoleInput
import net.mamoe.mirai.console.util.NamedSupervisorJob
import net.mamoe.mirai.console.util.SemVersion
import net.mamoe.mirai.utils.*
import java.io.PrintStream
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

    override val dataStorageForJvmPluginLoader =
        MultiFilePluginDataStorage(rootPath.resolve("data")).toMiraiCompose()

    override val dataStorageForBuiltIns =
        MultiFilePluginDataStorage(rootPath.resolve("data"))

    override val configStorageForJvmPluginLoader =
        MultiFilePluginDataStorage(rootPath.resolve("config")).toMiraiCompose()

    override val configStorageForBuiltIns =
        MultiFilePluginDataStorage(rootPath.resolve("config"))

    override val consoleInput: ConsoleInput =
        MiraiComposeInput

    override val consoleCommandSender =
        MiraiComposeSender

    override fun createLogger(identity: String?): MiraiLogger =
        MiraiComposeLogger(identity)

    override fun createLoginSolver(requesterBot: Long, configuration: BotConfiguration) =
        SwingSolver

    val composeFile = rootPath.resolve("compose").createDirectories()

    val logFiles = composeFile.resolve("log").createDirectories()

    val configFiles = composeFile.resolve("config").createDirectories()

    val model = Model()

    override fun preStart() {
        setSystemOut()
    }

    override fun postPhase(phase: String) {
        if (phase == "auto-login bots") {
            Bot.instances.forEach {
                model.bots.add(ComposeBot(it))
            }
        }
        if(phase == "finally post") {
            MiraiComposeView(model)

        }
    }
}



internal fun setSystemOut() {
    System.setOut(
        PrintStream(
            BufferedOutputStream(
                logger = MiraiLogger.create("stdout").run { ({ line: String? -> info(line) }) }
            ),
            false,
            "UTF-8"
        )
    )
    System.setErr(
        PrintStream(
            BufferedOutputStream(
                logger = MiraiLogger.create("stdout").run { ({ line: String? -> info(line) }) }
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


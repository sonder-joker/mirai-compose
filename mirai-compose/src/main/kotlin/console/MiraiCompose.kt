package com.youngerhousea.miraicompose.console

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.model.toComposeBot
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.ConsoleFrontEndImplementation
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.MiraiConsoleFrontEndDescription
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.data.MultiFilePluginDataStorage
import net.mamoe.mirai.console.data.PluginConfig
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.data.PluginDataHolder
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.PluginManager
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin
import net.mamoe.mirai.console.plugin.jvm.JvmPluginLoader
import net.mamoe.mirai.console.util.ConsoleInput
import net.mamoe.mirai.console.util.NamedSupervisorJob
import net.mamoe.mirai.console.util.SemVersion
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.MiraiLogger
import net.mamoe.mirai.utils.SwingSolver
import java.io.PrintStream
import java.nio.file.Path
import java.nio.file.Paths


@ConsoleFrontEndImplementation
class MiraiCompose : MiraiConsoleImplementation, MiraiComposeRepository, CoroutineScope by CoroutineScope(
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

    override val dataStorageForBuiltIns = MultiFilePluginDataStorage(rootPath.resolve("data"))

    override val configStorageForJvmPluginLoader = ReadablePluginConfigStorage(rootPath.resolve("config"))

    override val configStorageForBuiltIns = MultiFilePluginDataStorage(rootPath.resolve("config"))

    override val consoleInput: ConsoleInput =
        MiraiComposeInput

    override val consoleCommandSender: MiraiComposeSender =
        MiraiComposeSender

    override fun createLogger(identity: String?): MiraiLogger =
        MiraiComposeLogger(identity, _annotatedLogStorage)

    override fun createLoginSolver(requesterBot: Long, configuration: BotConfiguration) =
        SwingSolver

    override val botList: MutableList<ComposeBot> = mutableStateListOf()

    override var already by mutableStateOf(false)

    override val loadedPlugins: List<Plugin> by lazy { PluginManager.plugins }

    private val _annotatedLogStorage: MutableList<AnnotatedString> = mutableStateListOf()

    override val annotatedLogStorage: List<AnnotatedString> get() = _annotatedLogStorage

    override val JvmPlugin.data: List<PluginData>
        get() = if (this is PluginDataHolder) dataStorageForJvmPluginLoader[this] else error("Plugin is Not Holder!")

    override val JvmPlugin.config: List<PluginConfig>
        get() = if (this is PluginDataHolder) configStorageForJvmPluginLoader[this] else error("Plugin is Not Holder!")

    override fun preStart() {
        setSystemOut(MiraiConsole.mainLogger)
    }

    override fun postPhase(phase: String) {
        if (phase == "auto-login bots") {
            Bot.instances.map { it.toComposeBot() }.forEach {
                botList.add(it)
            }
        }
        if (phase == "load configurations") {
            ComposeDataScope.reloadAll()
        }
    }

    override fun postStart() {
        already = true
    }

}

object MiraiComposeDescription : MiraiConsoleFrontEndDescription {
    override val name: String = "MiraiCompose"
    override val vendor: String = "Noire"
    override val version: SemVersion = SemVersion("1.1.0")
}

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


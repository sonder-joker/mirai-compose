package com.youngerhousea.mirai.compose.console.impl

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.youngerhousea.mirai.compose.console.*
import kotlinx.coroutines.*
import mirai_compose.app.BuildConfig
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.MiraiConsoleFrontEndDescription
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.data.PluginConfig
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.data.PluginDataHolder
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin
import net.mamoe.mirai.console.plugin.jvm.JvmPluginLoader
import net.mamoe.mirai.console.util.ConsoleInput
import net.mamoe.mirai.console.util.SemVersion
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.utils.*
import java.lang.ref.WeakReference
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.coroutines.CoroutineContext
import kotlin.io.path.div

/**
 * Abstract mirai compose, this implementation for [MiraiConsoleImplementation] and [MiraiComposeInternal]
 *
 */
abstract class AbstractMiraiComposeImpl : MiraiComposeImplementation, MiraiComposeInternal {

    private val logger by lazy { createLogger("compose") }

    final override val rootPath: Path = Paths.get(System.getProperty("user.dir", ".")).toAbsolutePath()

    override val builtInPluginLoaders = listOf(lazy { JvmPluginLoader })

    override val frontEndDescription = MiraiComposeDescription

    override val dataStorageForJvmPluginLoader = ReadablePluginDataStorage(rootPath / "data")

    override val dataStorageForBuiltIns = ReadablePluginDataStorage(rootPath / "data")

    override val configStorageForJvmPluginLoader = ReadablePluginConfigStorage(rootPath / "config")

    override val configStorageForBuiltIns = ReadablePluginConfigStorage(rootPath / "config")

    override val consoleInput: ConsoleInput = MiraiComposeInput

    override val consoleCommandSender = MiraiConsoleSender(logger)

    override fun createLogger(identity: String?): MiraiLogger = LoggerCreator(identity ?: "Default")

    override val JvmPlugin.data: List<PluginData>
        get() = if (this is PluginDataHolder) dataStorageForJvmPluginLoader[this] else error("Plugin is Not Holder!")

    override val JvmPlugin.config: List<PluginConfig>
        get() = if (this is PluginDataHolder) configStorageForJvmPluginLoader[this] else error("Plugin is Not Holder!")

    override val loginSolverState: MutableState<LoginSolverState> = mutableStateOf(LoginSolverState.Nothing)

    private val picCaptcha = CompletableDeferred<String?>()

    private val sliderCaptcha = CompletableDeferred<String?>()

    private val unsafeDeviceLoginVerify = CompletableDeferred<String?>()

    override fun dispatch(login: Login) {
        when(login) {
            is Login.PicCaptcha -> picCaptcha.complete(login.string)
            is Login.SliderCaptcha -> sliderCaptcha.complete(login.string)
            is Login.UnsafeDevice -> unsafeDeviceLoginVerify.complete(login.string)
        }
    }

    override fun createLoginSolver(requesterBot: Long, configuration: BotConfiguration): LoginSolver =
        object : LoginSolver() {
            override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? {
                loginSolverState.value = LoginSolverState.PicCaptcha(bot, data)
                return picCaptcha.await()
            }

            override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? {
                loginSolverState.value = LoginSolverState.SliderCaptcha(bot, url)
                return sliderCaptcha.await()
            }

            override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? {
                loginSolverState.value = LoginSolverState.UnsafeDevice(bot, url)
                return unsafeDeviceLoginVerify.await()
            }
        }
}

/**
 * Create a [MiraiComposeImplementation], extend [ViewModelStoreOwner], [LifecycleOwner] and [CoroutineScope]
 *
 */
internal class MiraiComposeImpl(
    override val viewModelStore: ViewModelStore = ViewModelStoreImpl(),
) : AbstractMiraiComposeImpl(), MiraiComposeImplementation, ViewModelStoreOwner, LifecycleOwner, CoroutineScope {
    override val lifecycle: LifecycleRegistry = LifecycleRegistryImpl(WeakReference(this))

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext =
        CoroutineName("MiraiCompose") + CoroutineExceptionHandler { coroutineContext, throwable ->
            if (throwable is CancellationException) {
                return@CoroutineExceptionHandler
            }
            val coroutineName = coroutineContext[CoroutineName]?.name ?: "<unnamed>"
            MiraiConsole.mainLogger.error("Exception in coroutine $coroutineName", throwable)
        } + job

    override fun prePhase(phase: String) {
        when (phase) {
            "setup logger controller" -> {
                lifecycle.onEnterLoading()
            }
        }
    }

    override fun postPhase(phase: String) {
        when (phase) {
            "auto-login bots" -> {
                lifecycle.onFinishAutoLogin()
            }
            "finally post" -> {
                lifecycle.onFinishLoading()
            }
        }
    }

    override fun cancel() {
        lifecycle.onDestroy()
        viewModelStore.clean()
        cancel("Normal Exit")
    }
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

internal const val ANSI_RESET = "\u001b[0m"

@OptIn(MiraiInternalApi::class)
internal val LoggerCreator: (identity: String?) -> MiraiLogger = {
    PlatformLogger(identity = it, output = { line ->
        println(line + ANSI_RESET)
    })
}
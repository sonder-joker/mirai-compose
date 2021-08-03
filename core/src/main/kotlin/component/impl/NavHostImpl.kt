@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.youngerhousea.miraicompose.core.component.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.instancekeeper.getOrCreate
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value
import com.youngerhousea.miraicompose.core.component.NavHost
import com.youngerhousea.miraicompose.core.component.impl.about.AboutImpl
import com.youngerhousea.miraicompose.core.component.impl.bot.LoginImpl
import com.youngerhousea.miraicompose.core.component.impl.log.ConsoleLogImpl
import com.youngerhousea.miraicompose.core.component.impl.message.MessageImpl
import com.youngerhousea.miraicompose.core.component.impl.plugin.PluginsImpl
import com.youngerhousea.miraicompose.core.component.impl.setting.SettingImpl
import com.youngerhousea.miraicompose.core.configPath
import com.youngerhousea.miraicompose.core.console.*
import com.youngerhousea.miraicompose.core.data.BotItem
import com.youngerhousea.miraicompose.core.data.BotItemImpl
import com.youngerhousea.miraicompose.core.dataPath
import com.youngerhousea.miraicompose.core.root
import com.youngerhousea.miraicompose.core.utils.activeInstance
import com.youngerhousea.miraicompose.core.viewmodel.BotsViewModel
import com.youngerhousea.miraicompose.core.viewmodel.LogViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
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
import net.mamoe.mirai.utils.MiraiLogger
import java.nio.file.Path


internal abstract class ConsoleImpl :
    MiraiConsoleImplementation, AccessibleHolder, CoroutineScope by CoroutineScope(
    NamedSupervisorJob("MiraiCompose") + CoroutineExceptionHandler { coroutineContext, throwable ->
        if (throwable is CancellationException) {
            return@CoroutineExceptionHandler
        }
        val coroutineName = coroutineContext[CoroutineName]?.name ?: "<unnamed>"
        MiraiConsole.mainLogger.error("Exception in coroutine $coroutineName", throwable)
    }
) {
    final override val rootPath: Path = root

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

    override val JvmPlugin.data: List<PluginData>
        get() = if (this is PluginDataHolder) dataStorageForJvmPluginLoader[this] else error("Plugin is Not Holder!")

    override val JvmPlugin.config: List<PluginConfig>
        get() = if (this is PluginDataHolder) configStorageForJvmPluginLoader[this] else error("Plugin is Not Holder!")

    override fun postPhase(phase: String) {
        when (phase) {
            "auto-login bots" ->
                onAutoLogin(Bot.instances)

//            "load configurations" ->
//                ComposeDataScope.reloadAll()
//            "setup logger controller" -> {
//                assert(loggerController === ComposeLoggerController) { "?" }
//                ComposeDataScope.addAndReloadConfig(LoggerConfig)
//                ComposeLoggerController.initialized = true
//            }
        }
    }

    abstract fun onLoggerLog(log: Log)

    abstract fun onAutoLogin(instances: List<Bot>)

}

internal class NavHostImpl(
    component: ComponentContext,
    private val botsViewModel: BotsViewModel = component.instanceKeeper.getOrCreate { BotsViewModel() },
    private val logsViewModel: LogViewModel = component.instanceKeeper.getOrCreate { LogViewModel() }
) :  NavHost, ComponentContext by component {
    private val miraiComposeViewModel: MiraiComposeViewModel = instanceKeeper.getOrCreate(AccessibleHolder::class) {
        MiraiComposeViewModel(
            onLoggerLog = logsViewModel::addLog,
            onAutoLogin = botsViewModel::addBot,
            createLoginSolver = {
                router.push(Configuration.Login)
                val child = router.state.value.activeInstance
                assert(child is NavHost.Child.CLogin) { "Not route to login" }
                (child as NavHost.Child.CLogin).login as LoginImpl
            }
        )
    }

    private sealed class Configuration : Parcelable {
        object Message : Configuration()
        object Login : Configuration()
        object Setting : Configuration()
        object About : Configuration()
        object ConsoleLog : Configuration()
        object Plugin : Configuration()
    }

    private val router = router<Configuration, NavHost.Child>(
        initialConfiguration = Configuration.Login,
        handleBackButton = true,
        key = "NavHost"
    ) { config, componentContext ->
        when (config) {
            is Configuration.Login -> {
                NavHost.Child.CLogin(
                    LoginImpl(
                        componentContext,
                        onLoginSuccess = {
                            val botItemImpl = BotItemImpl(it)
                            botsViewModel.addBot(botItemImpl)
                            botsViewModel.setCurrentBot(botItemImpl)
                            onRouteMessage()
                        },
                    )
                )
            }
            is Configuration.Message ->
                NavHost.Child.CMessage(
                    MessageImpl(componentContext /*botList*/)
                )
            is Configuration.Plugin ->
                NavHost.Child.CPlugins(
                    PluginsImpl(
                        componentContext,
                        miraiComposeViewModel
                    )
                )
            is Configuration.Setting ->
                NavHost.Child.CSetting(
                    SettingImpl(
                        componentContext,
                    )
                )
            is Configuration.ConsoleLog ->
                NavHost.Child.CConsoleLog(
                    ConsoleLogImpl(
                        componentContext,
                        miraiComposeViewModel.logger
                    )
                )
            is Configuration.About ->
                NavHost.Child.CAbout(
                    AboutImpl(componentContext)
                )
        }
    }

    override val botList = botsViewModel.botList

    override val currentBot = botsViewModel.currentBot

    override val isExpand = MutableStateFlow(false)

    override val state: Value<RouterState<*, NavHost.Child>> get() = router.state


    override fun onRouteMessage() {
        router.push(Configuration.Message)
    }

    override fun onRoutePlugin() {
        router.push(Configuration.Plugin)
    }

    override fun onRouteSetting() {
        router.push(Configuration.Setting)
    }

    override fun onRouteLog() {
        router.push(Configuration.ConsoleLog)
    }

    override fun onRouteAbout() {
        router.push(Configuration.About)
    }


    override fun addNewBot() {
        router.push(Configuration.Login)
        dismissExpandMenu()
    }

    override fun openExpandMenu() {
        isExpand.value = true
    }

    override fun dismissExpandMenu() {
        isExpand.value = false
    }

    override fun onAvatarBoxClick() {
        if (currentBot.value != null)
            onRouteMessage()
        else
            addNewBot()
    }

    override fun onItemClick(item: BotItem) {
        botsViewModel.setCurrentBot(item)
        dismissExpandMenu()
    }
}



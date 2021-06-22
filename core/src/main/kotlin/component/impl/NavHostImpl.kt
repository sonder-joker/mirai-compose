package com.youngerhousea.miraicompose.core.component.impl

import com.arkivanov.decompose.router
import com.arkivanov.decompose.*
import com.arkivanov.decompose.instancekeeper.getOrCreate
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value
import com.youngerhousea.miraicompose.core.component.AvatarMenu
import com.youngerhousea.miraicompose.core.component.BotItem
import com.youngerhousea.miraicompose.core.component.NavHost
import com.youngerhousea.miraicompose.core.component.impl.about.AboutImpl
import com.youngerhousea.miraicompose.core.component.impl.bot.LoginImpl
import com.youngerhousea.miraicompose.core.component.impl.log.ConsoleLogImpl
import com.youngerhousea.miraicompose.core.component.impl.message.MessageImpl
import com.youngerhousea.miraicompose.core.component.impl.plugin.PluginsImpl
import com.youngerhousea.miraicompose.core.component.impl.setting.SettingImpl
import com.youngerhousea.miraicompose.core.console.MiraiCompose
import com.youngerhousea.miraicompose.core.console.MiraiComposeLogger
import com.youngerhousea.miraicompose.core.theme.ComposeSetting
import com.youngerhousea.miraicompose.core.utils.componentScope
import io.ktor.client.request.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.Mirai
import net.mamoe.mirai.utils.BotConfiguration
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

internal class NavHostImpl(
    component: ComponentContext,
) : NavHost, ComponentContext by component {
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
                NavHost.Child.CLogin(LoginImpl(
                    componentContext,
                    onLoginSuccess = ::onLoginSuccess,
                    composeFactory = { loginSolver ->
                        instanceKeeper.getOrCreate {
                            MiraiCompose { _: Long, _: BotConfiguration ->
                                loginSolver
                            }
                        }
                    }
                ))
            }
            is Configuration.Message ->
                NavHost.Child.CMessage(
                    MessageImpl(componentContext /*botList*/)
                )
            is Configuration.Plugin ->
                NavHost.Child.CPlugins(
                    PluginsImpl(
                        componentContext,
                    )
                )
            is Configuration.Setting ->
                NavHost.Child.CSetting(
                    SettingImpl(
                        componentContext,
                        ComposeSetting.AppTheme
                    )
                )
            is Configuration.ConsoleLog ->
                NavHost.Child.CConsoleLog(
                    ConsoleLogImpl(
                        componentContext,
                        MiraiComposeLogger.storage,
                        MiraiCompose.logger
                    )
                )
            is Configuration.About ->
                NavHost.Child.CAbout(
                    AboutImpl(componentContext)
                )
        }
    }


    private val scope = componentScope()

    override fun onRouteToSpecificBot(bot: BotItem) {
        scope.launch {
            currentBot.emit(bot)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun magic(): ConcurrentHashMap<Long, Bot>? {
        val instance = Bot.Companion::class.memberProperties.find { it.name == "_instances" }
        instance?.let {
            it.isAccessible = true
            return it.get(Bot.Companion) as ConcurrentHashMap<Long, Bot>?
        } ?: error("Reflect error")
    }

    private val map = magic()

    override val botList: List<BotItem>
        get() = map?.values?.toList()?.mapIndexed { indexed, bot ->
            BotItemImpl(childContext("bot:${indexed}"), bot)
        } ?: emptyList()

    override val state: Value<RouterState<*, NavHost.Child>> get() = router.state

    override val currentBot = MutableStateFlow(botList.firstOrNull())

    override val avatarMenu: AvatarMenu = AvatarMenuImpl(
        childContext("avatarMenu"),
        _addNewBot = {
            router.push(Configuration.Login)
        },
        _onRouteMessage = ::onRouteMessage,
        currentBot = currentBot
    )

    private fun onLoginSuccess(bot: Bot) {
        router.push(Configuration.Message)
    }

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

}

class AvatarMenuImpl(
    component: ComponentContext,
    private val _addNewBot: () -> Unit,
    val _onRouteMessage: () -> Unit,
    override val currentBot: MutableStateFlow<BotItem?>,
) : AvatarMenu, ComponentContext by component {
    override val isExpand = MutableStateFlow(false)
    override val botList: List<BotItem>
        get() = emptyList()

    private val scope = componentScope()

    override fun addNewBot() {
        _addNewBot()
    }

    override fun openExpandMenu() {
//        TODO("Not yet implemented")
    }

    override fun dismissExpandMenu() {
//        TODO("Not yet implemented")
    }

    override fun onAvatarBoxClick() {
        if (currentBot.value != null)
            _onRouteMessage()
        else
            addNewBot()
    }

    override fun onItemClick(item: BotItem) {
        scope.launch {
            currentBot.emit(item)
            isExpand.emit(false)
        }
    }
}


class BotItemImpl(
    component: ComponentContext,
    override val bot: Bot,
) : BotItem, ComponentContext by component {

    override var avatar: ByteArray? = null

    init {
        bot.launch {
            avatar = Mirai.Http.get(bot.avatarUrl)
        }
    }
}
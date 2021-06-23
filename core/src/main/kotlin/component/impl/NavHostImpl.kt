package com.youngerhousea.miraicompose.core.component.impl

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
import com.youngerhousea.miraicompose.core.utils.getValue
import com.youngerhousea.miraicompose.core.utils.setValue
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
                    onLoginSuccess = { onRouteMessage() },
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

    override val state: Value<RouterState<*, NavHost.Child>> get() = router.state

    override val avatarMenu: AvatarMenu = AvatarMenuImpl(
        childContext("avatarMenu"),
        _addNewBot = {
            router.push(Configuration.Login)
        },
        _onAvatarBoxClick = ::onRouteMessage,
    )


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
    private val _onAvatarBoxClick: () -> Unit,
) : AvatarMenu, ComponentContext by component {

    private val botList get() = magic()

    override val model = MutableStateFlow(
        AvatarMenu.Model(
            currentBot = botList.firstOrNull(), isExpand = false, botList = botList
        )
    )

    var delegateModel by model


    @Suppress("UNCHECKED_CAST")
    private fun magic(): List<BotItem> {
        val instance = Bot.Companion::class.memberProperties.find { it.name == "_instances" }
        val data = instance?.let {
            it.isAccessible = true
            it.get(Bot.Companion) as ConcurrentHashMap<Long, Bot>?
        } ?: error("Reflect error")
        return data.values.toList().mapIndexed { indexed, bot ->
            BotItemImpl(childContext("bot:${indexed}"), bot)
        }
    }

    override fun addNewBot() {
        _addNewBot()
        dismissExpandMenu()
    }

    override fun openExpandMenu() {
        delegateModel = delegateModel.copy(isExpand = true)
    }

    override fun dismissExpandMenu() {
        delegateModel = delegateModel.copy(isExpand = false)
    }

    override fun onAvatarBoxClick() {
        if (model.value.currentBot != null)
            _onAvatarBoxClick()
        else
            addNewBot()
    }

    override fun onItemClick(item: BotItem) {
        delegateModel = delegateModel.copy(currentBot = item)
        dismissExpandMenu()
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
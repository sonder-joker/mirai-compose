package com.youngerhousea.miraicompose.core.component.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.value.Value
import com.youngerhousea.miraicompose.core.component.NavHost
import com.youngerhousea.miraicompose.core.component.impl.about.AboutImpl
import com.youngerhousea.miraicompose.core.component.impl.bot.LoginImpl
import com.youngerhousea.miraicompose.core.component.impl.message.MessageImpl
import com.youngerhousea.miraicompose.core.component.impl.log.ConsoleLogImpl
import com.youngerhousea.miraicompose.core.component.impl.plugin.PluginsImpl
import com.youngerhousea.miraicompose.core.component.impl.setting.SettingImpl
import com.youngerhousea.miraicompose.core.console.ComposeBot
import com.youngerhousea.miraicompose.core.console.ComposeLog
import com.youngerhousea.miraicompose.core.console.MiraiCompose
import com.youngerhousea.miraicompose.core.console.toComposeBot
import com.youngerhousea.miraicompose.core.theme.ComposeSetting
import net.mamoe.mirai.Bot


class NavHostImpl(
    component: ComponentContext,
) : NavHost, ComponentContext by component {

    private val _botList: MutableList<ComposeBot> = MiraiCompose.botList

    //TODO:
//    private var _currentBot = _botList.firstOrNull()

    private val router = router<NavHost.Configuration, ComponentContext>(
        initialConfiguration = NavHost.Configuration.Login,
        handleBackButton = true,
        key = "NavHost",
        childFactory = { config, componentContext ->
            when (config) {
                is NavHost.Configuration.Login ->
                    LoginImpl(componentContext, onLoginSuccess = ::onLoginSuccess)
                is NavHost.Configuration.Message ->
                    MessageImpl(componentContext, botList)
                is NavHost.Configuration.Plugin ->
                    PluginsImpl(
                        componentContext,
                    )
                is NavHost.Configuration.Setting ->
                    SettingImpl(
                        componentContext,
                        ComposeSetting.AppTheme
                    )
                is NavHost.Configuration.ConsoleLog ->
                    ConsoleLogImpl(
                        componentContext,
                        ComposeLog.storage,
                        MiraiCompose.logger
                    )
                is NavHost.Configuration.About ->
                    AboutImpl(componentContext)
            }
        }
    )


    override val botList: List<ComposeBot> get() = _botList

    override val currentBot: ComposeBot? get() = /*_currentBot*/ null

    override val state: Value<RouterState<NavHost.Configuration, ComponentContext>> get() = router.state

    // 当机器人登录成功
    private fun onLoginSuccess(bot: Bot) {
        val composeBot = bot.toComposeBot()
//        _currentBot = composeBot
        _botList.add(composeBot)
        router.push(NavHost.Configuration.Message)
    }

    override fun addNewBot() {
        router.push(NavHost.Configuration.Login)
    }

    override fun onRouteToSpecificBot(bot: ComposeBot) {
//        _currentBot = bot
    }

    override fun onRouteMessage() {
        router.push(NavHost.Configuration.Message)
    }

    override fun onRoutePlugin() {
        router.push(NavHost.Configuration.Plugin)
    }

    override fun onRouteSetting() {
        router.push(NavHost.Configuration.Setting)
    }

    override fun onRouteLog() {
        router.push(NavHost.Configuration.ConsoleLog)
    }

    override fun onRouteAbout() {
        router.push(NavHost.Configuration.About)
    }
}

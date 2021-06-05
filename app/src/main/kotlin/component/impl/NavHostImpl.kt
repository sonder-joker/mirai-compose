package com.youngerhousea.miraicompose.component.impl

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.value.Value
import com.youngerhousea.miraicompose.component.Configuration
import com.youngerhousea.miraicompose.component.NavHost
import com.youngerhousea.miraicompose.component.impl.about.AboutImpl
import com.youngerhousea.miraicompose.component.impl.bot.LoginImpl
import com.youngerhousea.miraicompose.component.impl.bot.MessageImpl
import com.youngerhousea.miraicompose.component.impl.log.ConsoleLogImpl
import com.youngerhousea.miraicompose.component.impl.plugin.PluginsImpl
import com.youngerhousea.miraicompose.component.impl.setting.SettingImpl
import com.youngerhousea.miraicompose.console.ComposeLog
import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.console.ComposeBot
import com.youngerhousea.miraicompose.console.toComposeBot
import com.youngerhousea.miraicompose.theme.ComposeSetting
import com.youngerhousea.miraicompose.ui.about.AboutUi
import com.youngerhousea.miraicompose.ui.bot.LoginUi
import com.youngerhousea.miraicompose.ui.bot.MessageUi
import com.youngerhousea.miraicompose.ui.log.ConsoleLogUi
import com.youngerhousea.miraicompose.ui.plugin.PluginsUi
import com.youngerhousea.miraicompose.ui.setting.SettingUi
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent
import net.mamoe.mirai.Bot

class NavHostImpl(
    component: ComponentContext,
) : NavHost, ComponentContext by component {

    private val _botList: MutableList<ComposeBot> = MiraiCompose.botList

    private var _currentBot by mutableStateOf(_botList.firstOrNull())

    private val router = router<Configuration, Component>(
        initialConfiguration = Configuration.Login,
        handleBackButton = true,
        key = "NavHost",
        childFactory = { config, componentContext ->
            when (config) {
                is Configuration.Login ->
                    LoginImpl(componentContext, onLoginSuccess = ::onLoginSuccess).asComponent { LoginUi(it) }
                is Configuration.Message ->
                    MessageImpl(componentContext, botList).asComponent { MessageUi(it) }
                is Configuration.Plugin ->
                    PluginsImpl(
                        componentContext,
                    ).asComponent { PluginsUi(it) }
                is Configuration.Setting ->
                    SettingImpl(
                        componentContext,
                        ComposeSetting.AppTheme
                    ).asComponent { SettingUi(it) }
                is Configuration.ConsoleLog ->
                    ConsoleLogImpl(
                        componentContext,
                        ComposeLog.storage,
                        MiraiCompose.logger
                    ).asComponent { ConsoleLogUi(it) }
                is Configuration.About ->
                    AboutImpl(componentContext).asComponent { AboutUi(it) }
            }
        }
    )


    override val botList: List<ComposeBot> get() = _botList

    override val currentBot: ComposeBot? get() = _currentBot

    override val state: Value<RouterState<Configuration, Component>> get() = router.state

    // 当机器人登录成功
    private fun onLoginSuccess(bot: Bot) {
        val composeBot = bot.toComposeBot()
        _currentBot = composeBot
        _botList.add(composeBot)
        router.push(Configuration.Message)
    }

    override fun addNewBot() {
        router.push(Configuration.Login)
    }

    override fun onRouteToSpecificBot(bot: ComposeBot) {
        _currentBot = bot
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

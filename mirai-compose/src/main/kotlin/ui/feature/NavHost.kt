package com.youngerhousea.miraicompose.ui.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.console.MiraiComposeLogger
import com.youngerhousea.miraicompose.console.MiraiConsoleRepository
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.ui.common.AvatarColumn
import com.youngerhousea.miraicompose.ui.common.SelectEdgeText
import com.youngerhousea.miraicompose.ui.feature.about.About
import com.youngerhousea.miraicompose.ui.feature.about.AboutUi
import com.youngerhousea.miraicompose.ui.feature.bot.BotChoose
import com.youngerhousea.miraicompose.ui.feature.bot.BotChooseUi
import com.youngerhousea.miraicompose.ui.feature.log.AllLog
import com.youngerhousea.miraicompose.ui.feature.log.AllLogUi
import com.youngerhousea.miraicompose.ui.feature.plugin.LoadedPlugin
import com.youngerhousea.miraicompose.ui.feature.plugin.LoadedPluginUi
import com.youngerhousea.miraicompose.ui.feature.setting.Setting
import com.youngerhousea.miraicompose.ui.feature.setting.SettingUi
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent

class NavHost(
    component: ComponentContext,
    val compose: MiraiConsoleRepository,
) : ComponentContext by component {
    var currentBot: ComposeBot? by mutableStateOf(instance.firstOrNull())

    val instance get() = compose.composeBotList

    val state get() = router.state

    private val router = router<Config, Component>(
        initialConfiguration = Config.Bot(currentBot),
        handleBackButton = true,
        componentFactory = { config, componentContext ->
            when (config) {
                is Config.Bot ->
                    BotChoose(
                        componentContext,
                        bot = config.bot,
                    ).asComponent { BotChooseUi(it) }
                is Config.Setting ->
                    Setting(
                        componentContext
                    ).asComponent { SettingUi() }
                is Config.About ->
                    About(
                        componentContext
                    ).asComponent { AboutUi(it) }
                is Config.Log ->
                    AllLog(
                        componentContext,
                        loggerStorage = MiraiComposeLogger.loggerStorage,
                        logger = MiraiComposeLogger.out
                    ).asComponent { AllLogUi(it) }
                is Config.Plugin -> {
                    LoadedPlugin(
                        componentContext,
                        repository = compose
                    ).asComponent { LoadedPluginUi(it) }
                }
            }
        }
    )

    sealed class Config : Parcelable {
        class Bot(val bot: ComposeBot?) : Config()
        object Setting : Config()
        object About : Config()
        object Log : Config()
        object Plugin : Config()
    }

    fun onRouteBot(bot: ComposeBot) {
        router.push(Config.Bot(bot))
    }

    fun onRouteSetting() {
        router.push(Config.Setting)
    }

    fun onRouteAbout() {
        router.push(Config.About)
    }

    fun onRouteLog() {
        router.push(Config.Log)
    }

    fun onRoutePlugin() {
        router.push(Config.Plugin)
    }

}

@Composable
fun NavHostUi(navHost: NavHost) {
    Row(Modifier.fillMaxSize()) {
        Children(
            navHost.state,
        ) { child, config ->
            Column(
                Modifier
                    .requiredWidth(160.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colors.primary),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AvatarColumn(
                    navHost.instance,
                    navHost.currentBot,
                    onItemSelected = {
                        navHost.currentBot = it
                        navHost.onRouteBot(it)
                    }
                )
                SelectEdgeText(
                    "Robot",
                    isWishWindow = config is NavHost.Config.Bot,
                    onClick = {
                        navHost.currentBot?.let {
                            navHost.onRouteBot(it)
                        }
                    }
                )
                SelectEdgeText(
                    "Plugin",
                    isWishWindow = config is NavHost.Config.Plugin,
                    onClick = navHost::onRoutePlugin
                )
                SelectEdgeText(
                    "Setting",
                    isWishWindow = config is NavHost.Config.Setting,
                    onClick = navHost::onRouteSetting
                )
                SelectEdgeText(
                    "Log",
                    isWishWindow = config is NavHost.Config.Log,
                    onClick = navHost::onRouteLog
                )
                SelectEdgeText(
                    "About",
                    isWishWindow = config is NavHost.Config.About,
                    onClick = navHost::onRouteAbout
                )
            }
            child()
        }
    }
}
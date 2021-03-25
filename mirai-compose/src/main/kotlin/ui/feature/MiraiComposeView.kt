package com.youngerhousea.miraicompose.ui.feature

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.console.MiraiComposeLogger
import com.youngerhousea.miraicompose.console.MiraiConsoleRepository
import com.youngerhousea.miraicompose.future.Application
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.theme.ComposeSetting
import com.youngerhousea.miraicompose.theme.ResourceImage
import com.youngerhousea.miraicompose.ui.common.SelectEdgeText
import com.youngerhousea.miraicompose.ui.common.Spacer
import com.youngerhousea.miraicompose.ui.feature.about.About
import com.youngerhousea.miraicompose.ui.feature.about.AboutUi
import com.youngerhousea.miraicompose.ui.feature.bot.BotChoose
import com.youngerhousea.miraicompose.ui.feature.bot.BotChooseUi
import com.youngerhousea.miraicompose.ui.feature.log.AllLog
import com.youngerhousea.miraicompose.ui.feature.log.AllLogUi
import com.youngerhousea.miraicompose.ui.feature.plugin.LoadedPluginUi
import com.youngerhousea.miraicompose.ui.feature.plugin.LoadedPlugin
import com.youngerhousea.miraicompose.ui.feature.setting.Setting
import com.youngerhousea.miraicompose.ui.feature.setting.SettingUi
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start

fun MiraiComposeView() = Application {
    val compose = remember { MiraiCompose() }

    var circleSize by remember { mutableStateOf(50f) }
    val animateCircleSize by animateFloatAsState(circleSize)

    LaunchedEffect(Unit) {
        compose.start()
    }
    if (compose.isReady)
        ComposableWindow(
            title = "",
            size = IntSize(1280, 768),
            icon = ResourceImage.icon,
            onDismissRequest = {
                compose.cancel()
            }
        ) {
            DesktopMaterialTheme(
                colors = ComposeSetting.AppTheme.materialLight
            ) {
                rememberRootComponent { componentContext ->
                    NavHost(componentContext, compose)
                }.asComponent { NavHostUi(it) }()
            }
        }
    else
        ComposableWindow(
            undecorated = true,
            size = IntSize(400, 400),
        ) {
            LaunchedEffect(Unit) {
                while (true) {
                    delay(10)
                    circleSize++
                    if (circleSize > 200f) {
                        circleSize = 0f
                    }
                }
            }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color(0xffe8e0cb))
            ) {
                drawCircle(Color.Red, radius = animateCircleSize, style = Stroke(1.5f))
                drawCircle(Color.Red, radius = 100f, style = Stroke(1.5f))
                drawCircle(Color.Red, radius = 150f, style = Stroke(1.5f))
            }
        }
}


class NavHost(
    component: ComponentContext,
    val compose: MiraiConsoleRepository,
) : ComponentContext by component {
    private var currentBot: ComposeBot? = null
    private val instance = ComposeBot.instances

    val state get() = router.state

    sealed class Config : Parcelable {
        object Bot : Config()
        object Setting : Config()
        object About : Config()
        object Log : Config()
        object Plugin : Config()
    }

    val currentBotMessage get() = currentBot?.messagePerMinute
    val allBotMessage get() = instance.fold(0f) { acc, composeBot -> acc + composeBot.messagePerMinute }

    private fun onBotSelected(bot: ComposeBot) {
        currentBot = bot
    }

    fun onRouteBot() {
        router.push(Config.Bot)
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

    private val router = router<Config, Component>(
        initialConfiguration = Config.Bot,
        handleBackButton = true,
        componentFactory = { config, componentContext ->
            when (config) {
                is Config.Bot ->
                    BotChoose(
                        componentContext,
                        model = instance,
                        onSelectedBot = ::onBotSelected
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
        },
    )


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
                Spacer(
                    navHost.currentBotMessage,
                    navHost.allBotMessage
                )
                SelectEdgeText(
                    "Robot",
                    isWishWindow = config is NavHost.Config.Bot,
                    onClick = navHost::onRouteBot
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
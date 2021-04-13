package com.youngerhousea.miraicompose.ui.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.console.MiraiComposeLogger
import com.youngerhousea.miraicompose.console.MiraiConsoleRepository
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.ui.feature.about.About
import com.youngerhousea.miraicompose.ui.feature.about.AboutUi
import com.youngerhousea.miraicompose.ui.feature.bot.BotChoose
import com.youngerhousea.miraicompose.ui.feature.bot.BotChooseUi
import com.youngerhousea.miraicompose.ui.feature.log.AllLog
import com.youngerhousea.miraicompose.ui.feature.log.AllLogUi
import com.youngerhousea.miraicompose.ui.feature.plugin.Plugins
import com.youngerhousea.miraicompose.ui.feature.plugin.PluginsUi
import com.youngerhousea.miraicompose.ui.feature.setting.Setting
import com.youngerhousea.miraicompose.ui.feature.setting.SettingUi
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent
import com.youngerhousea.miraicompose.utils.items

class NavHost(
    component: ComponentContext,
    val compose: MiraiConsoleRepository,
) : ComponentContext by component {
    private var _currentBot: ComposeBot? by mutableStateOf(instance.firstOrNull())

    private inline val _instance get() = compose.composeBotList

    private var _index by mutableStateOf(0)

    val currentBot get() = _currentBot

    val instance: List<ComposeBot> get() = _instance

    val state get() = router.state

    val index get() = _index

    private val router = router<Config, Component>(
        initialConfiguration = Config.Bot(_currentBot),
        handleBackButton = true,
        childFactory = { config, componentContext ->
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
                    Plugins(
                        componentContext,
                        repository = compose
                    ).asComponent { PluginsUi(it) }
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

    fun onRouteNewBot() {
        _index = 0

        val newBot = ComposeBot()
        _instance.add(newBot)

        _currentBot = newBot
    }

    fun onRouteCurrentBot() {
        _index = 0

        router.push(Config.Bot(_currentBot))
    }

    fun onRouteSpecificBot(composeBot: ComposeBot) {
        _index = 0

        _currentBot = composeBot

        router.push(Config.Bot(_currentBot))
    }


    fun onRoutePlugin() {
        _index = 1

        router.push(Config.Plugin)
    }

    fun onRouteSetting() {
        _index = 2

        router.push(Config.Setting)
    }

    fun onRouteLog() {
        _index = 3

        router.push(Config.Log)
    }

    fun onRouteAbout() {
        _index = 4

        router.push(Config.About)
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun NavHostUi(navHost: NavHost) {
    Row(Modifier.fillMaxSize()) {
        SideColumn(navHost)
        Children(
            navHost.state, crossfade()
        ) { child ->
            child.instance()
        }
    }
}

@Composable
private fun SideColumn(navHost: NavHost) {
    Column(
        Modifier
            .width(160.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colors.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AvatarWithMenu(
            navHost.instance,
            navHost.currentBot,
            onMenuItemSelected = navHost::onRouteSpecificBot,
            onNewItemButtonSelected = navHost::onRouteNewBot,
            onClick = navHost::onRouteCurrentBot
        )
        SelectEdgeText(
            "Robot",
            isWishWindow = navHost.index == 0,
            onClick = navHost::onRouteCurrentBot
        )
        SelectEdgeText(
            "Plugin",
            isWishWindow = navHost.index == 1,
            onClick = navHost::onRoutePlugin
        )
        SelectEdgeText(
            "Setting",
            isWishWindow = navHost.index == 2,
            onClick = navHost::onRouteSetting
        )
        SelectEdgeText(
            "Log",
            isWishWindow = navHost.index == 3,
            onClick = navHost::onRouteLog
        )
        SelectEdgeText(
            "About",
            isWishWindow = navHost.index == 4,
            onClick = navHost::onRouteAbout
        )
    }
}

@Composable
private fun AvatarWithMenu(
    composeBotList: List<ComposeBot>,
    currentBot: ComposeBot?,
    onMenuItemSelected: (ComposeBot) -> Unit,
    onNewItemButtonSelected: () -> Unit,
    onClick: () -> Unit
) {
    var isExpand by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .combinedClickable(
                    onLongClick = { isExpand = !isExpand },
                    onClick = onClick
                )
                .fillMaxWidth()
                .requiredHeight(80.dp)
        ) {
            currentBot?.let {
                BotItem(currentBot)
            } ?: Text("No item")
        }

        DropdownMenu(isExpand, onDismissRequest = { /*isExpand = !isExpand*/ }) {
            DropdownMenuItem(onClick = { isExpand = !isExpand }) {
                Text("Exit")
            }

            DropdownMenuItem(onClick = {
                isExpand = !isExpand
                onNewItemButtonSelected()
            }) {
                Text("Add")
            }

            items(composeBotList) {
                DropdownMenuItem(onClick = {
                    isExpand = !isExpand
                    onMenuItemSelected(it)
                }) {
                    BotItem(it)
                }
            }
        }
    }
}


@Composable
private fun SelectEdgeText(text: String, isWishWindow: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .requiredHeight(80.dp)
            .background(if (isWishWindow) MaterialTheme.colors.background else MaterialTheme.colors.primary),
        contentAlignment = Alignment.Center
    ) {
        if (isWishWindow)
            Text(text, style = MaterialTheme.typography.subtitle1)
        else
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(text, style = MaterialTheme.typography.subtitle1)
            }
    }
}

@Composable
private fun BotItem(
    bot: ComposeBot,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .aspectRatio(2f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.weight(1f))
        Surface(
            modifier = Modifier
                .weight(3f, fill = false),
            shape = CircleShape,
            color = MaterialTheme.colors.surface.copy(alpha = 0.12f)
        ) {
            Image(bot.avatar, null)
        }

        Column(
            Modifier
                .weight(6f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(bot.nick, fontWeight = FontWeight.Bold, maxLines = 1)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(bot.id, style = MaterialTheme.typography.body2)
            }
        }
        Spacer(Modifier.weight(1f))
    }
}
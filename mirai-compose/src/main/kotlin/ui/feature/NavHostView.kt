package com.youngerhousea.miraicompose.ui.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.arkivanov.decompose.instancekeeper.getOrCreate
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.consume
import com.youngerhousea.miraicompose.console.MiraiComposeRepository
import com.youngerhousea.miraicompose.future.inject
import com.youngerhousea.miraicompose.theme.ComposeSetting
import com.youngerhousea.miraicompose.ui.feature.about.About
import com.youngerhousea.miraicompose.ui.feature.about.AboutUi
import com.youngerhousea.miraicompose.ui.feature.bot.BotOnline
import com.youngerhousea.miraicompose.ui.feature.bot.BotOnlineUi
import com.youngerhousea.miraicompose.ui.feature.bot.Login
import com.youngerhousea.miraicompose.ui.feature.bot.LoginUi
import com.youngerhousea.miraicompose.ui.feature.log.MainLog
import com.youngerhousea.miraicompose.ui.feature.log.MainLogUi
import com.youngerhousea.miraicompose.ui.feature.plugin.Plugins
import com.youngerhousea.miraicompose.ui.feature.plugin.PluginsUi
import com.youngerhousea.miraicompose.ui.feature.setting.Setting
import com.youngerhousea.miraicompose.ui.feature.setting.SettingUi
import com.youngerhousea.miraicompose.utils.*
import com.youngerhousea.miraicompose.utils.SkiaImageDecode
import io.ktor.client.request.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.Mirai
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.event.events.BotEvent


class NavHost(
    component: ComponentContext,
) : ComponentContext by component {
    private val miraiComposeRepository: MiraiComposeRepository by inject()

    private var _navigationIndex by mutableStateOf(0)

    val navigationIndex get() = _navigationIndex

    val state get() = router.state

    // 表示当前bot的list
    private val _botList get() = miraiComposeRepository.botList

    val botList: List<ComposeBot> get() = _botList

    private val _currentBot by lazy { mutableStateOf(_botList.firstOrNull()) }

    var currentBot by _currentBot

    private val router = router<Config, Component>(
        initialConfiguration = Config.Login,
        handleBackButton = true,
        key = "NavHost",
        childFactory = { config, componentContext ->
            when (config) {
                is Config.Login ->
                    Login(
                        componentContext,
                        onLoginSuccess = { bot ->
                            val composeBot = bot.toComposeBot()
                            currentBot = composeBot
                            _botList.add(composeBot)
                            onMenuToSpecificBot(composeBot)
                        }
                    ).asComponent { LoginUi(it) }
                is Config.OnlineBot ->
                    BotOnline(
                        componentContext,
                        config.bot
                    ).asComponent { BotOnlineUi(it) }
                is Config.Setting ->
                    Setting(
                        componentContext,
                        theme = ComposeSetting.AppTheme
                    ).asComponent { SettingUi(it) }
                is Config.About ->
                    About(
                        componentContext
                    ).asComponent { AboutUi(it) }
                is Config.Log ->
                    MainLog(
                        componentContext,
                        loggerStorage = miraiComposeRepository.annotatedLogStorage,
                        logger = MiraiConsole.mainLogger
                    ).asComponent { MainLogUi(it) }
                is Config.Plugin -> {
                    Plugins(
                        componentContext,
                        miraiComposeRepository
                    ).asComponent { PluginsUi(it) }
                }
            }
        }
    )

    sealed class Config : Parcelable {
        class OnlineBot(val bot: ComposeBot) : Config()
        object Login : Config()
        object Setting : Config()
        object About : Config()
        object Log : Config()
        object Plugin : Config()
    }

    //menu action
    fun onMenuAddNewBot() {
        _navigationIndex = 0
        router.push(Config.Login)
    }

    fun onMenuToSpecificBot(bot: ComposeBot) {
        router.push(Config.OnlineBot(bot))
    }

    //  分别对于SideColumn的五个index
    fun onRouteBot() {
        _navigationIndex = 0
        currentBot?.let { router.push(Config.OnlineBot(it)) } ?: router.push(Config.Login)
    }

    fun onRoutePlugin() {
        _navigationIndex = 1

        router.push(Config.Plugin)
    }

    fun onRouteSetting() {
        _navigationIndex = 2

        router.push(Config.Setting)
    }

    fun onRouteLog() {
        _navigationIndex = 3

        router.push(Config.Log)
    }

    fun onRouteAbout() {
        _navigationIndex = 4

        router.push(Config.About)
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun NavHostUi(navHost: NavHost) {
    Column(Modifier.fillMaxSize()) {
        TopAppBar(Modifier.height(80.dp)) { SideRow(navHost) }
        Children(
            navHost.state, crossfade()
        ) { child ->
            child.instance()
        }
    }
}


@Composable
private fun SideRow(navHost: NavHost) {
    AvatarWithMenu(
        navHost.botList,
        navHost.currentBot,
        onMenuItemSelected = navHost::onMenuToSpecificBot,
        onNewItemButtonSelected = navHost::onMenuAddNewBot,
    )
    SelectEdgeText(
        "Robot",
        isWishWindow = navHost.navigationIndex == 0,
        onClick = navHost::onRouteBot
    )
    SelectEdgeText(
        "Plugin",
        isWishWindow = navHost.navigationIndex == 1,
        onClick = navHost::onRoutePlugin
    )
    SelectEdgeText(
        "Setting",
        isWishWindow = navHost.navigationIndex == 2,
        onClick = navHost::onRouteSetting
    )
    SelectEdgeText(
        "Log",
        isWishWindow = navHost.navigationIndex == 3,
        onClick = navHost::onRouteLog
    )
    SelectEdgeText(
        "About",
        isWishWindow = navHost.navigationIndex == 4,
        onClick = navHost::onRouteAbout
    )
}

@Composable
private fun AvatarWithMenu(
    composeBotList: List<ComposeBot>,
    currentBot: ComposeBot?,
    onMenuItemSelected: (bot: ComposeBot) -> Unit,
    onNewItemButtonSelected: () -> Unit,
) {
    var isExpand by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .clickable {
                    isExpand = !isExpand
                }
        ) {
            currentBot?.let {
                BotItem(currentBot)
            } ?: Text("No item")
        }

        DropdownMenu(isExpand, onDismissRequest = { isExpand = !isExpand }) {
            DropdownMenuItem(onClick = { isExpand = !isExpand }) {
                Text("Logout")
            }

            DropdownMenuItem(onClick = {
                onNewItemButtonSelected()
                isExpand = !isExpand
            }) {
                Text("Add")
            }

            items(composeBotList) { item ->
                DropdownMenuItem(onClick = {
                    onMenuItemSelected(item)
                    isExpand = !isExpand
                }) {
                    BotItem(item)
                }
            }
        }
    }
}


@Composable
private fun SelectEdgeText(text: String, isWishWindow: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .clickable(onClick = onClick),
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
                Text("${bot.id}", style = MaterialTheme.typography.body2)
            }
        }
        Spacer(Modifier.weight(1f))
    }
}

interface ComposeBot : Bot {
    val avatar: ImageBitmap

    val eventList: List<BotEvent>
}

fun Bot.toComposeBot(): ComposeBot = ComposeBotImpl(this)

@OptIn(ExperimentalCoroutinesApi::class)
class ComposeBotImpl(bot: Bot) : Bot by bot, ComposeBot {
    private var _avatar by mutableStateOf(ImageBitmap(200, 200))

    private val _eventList = mutableStateListOf<BotEvent>()

    override val eventList: List<BotEvent> get() = _eventList

    init {
        launch {
            _avatar = SkiaImageDecode(
                Mirai.Http.get(avatarUrl) {
                    header("Connection", "close")
                }
            )
        }
        launch {
            bot.eventChannel.asChannel().receiveAsFlow().collect {
                _eventList.add(it)
            }
        }

    }

    override val avatar get() = _avatar
}


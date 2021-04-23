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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.console.MiraiComposeRepository
import com.youngerhousea.miraicompose.future.inject
import com.youngerhousea.miraicompose.theme.ComposeSetting
import com.youngerhousea.miraicompose.ui.feature.about.About
import com.youngerhousea.miraicompose.ui.feature.about.AboutUi
import com.youngerhousea.miraicompose.ui.feature.bot.BotState
import com.youngerhousea.miraicompose.ui.feature.bot.BotStateUi
import com.youngerhousea.miraicompose.ui.feature.log.MainLog
import com.youngerhousea.miraicompose.ui.feature.log.MainLogUi
import com.youngerhousea.miraicompose.ui.feature.log.onRightClick
import com.youngerhousea.miraicompose.ui.feature.plugin.Plugins
import com.youngerhousea.miraicompose.ui.feature.plugin.PluginsUi
import com.youngerhousea.miraicompose.ui.feature.setting.Setting
import com.youngerhousea.miraicompose.ui.feature.setting.SettingUi
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent
import com.youngerhousea.miraicompose.utils.itemsWithIndexed
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.MiraiConsole


class NavHost(
    component: ComponentContext,
) : ComponentContext by component {
    private val miraiComposeRepository: MiraiComposeRepository by inject()

    private var _botIndex by mutableStateOf(0)

    private var _navigationIndex by mutableStateOf(0)

    // 表示当前bot的list
    val botList: List<Bot?> get() = miraiComposeRepository.botList

    val currentBot get() = botList.getOrNull(_botIndex)

    val state get() = router.state

    val navigationIndex get() = _navigationIndex

    private val router = router<Config, Component>(
        initialConfiguration = Config.BotR(null),
        handleBackButton = true,
        key = "NavHost",
        childFactory = { config, componentContext ->
            when (config) {
                is Config.BotR -> {
                    BotState(
                        componentContext,
                        bot = config.bot,
                        // need clear
                        index = _botIndex,
                        onLoginSuccess = { index, bot ->
                            miraiComposeRepository.setNullToBot(index, bot)
                        }
                    ).asComponent { BotStateUi(it) }
                }
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
        class BotR(val bot: Bot?) : Config()
        object Setting : Config()
        object About : Config()
        object Log : Config()
        object Plugin : Config()
    }

    fun onMenuAddNewBot() {
        miraiComposeRepository.addBot(null)
        _botIndex = botList.lastIndex
        onRouteBot()
    }

    fun onMenuLogOutBot() {
        // TODO: bot.logout()
        miraiComposeRepository.botList.removeAt(_botIndex)
        if (miraiComposeRepository.botList.isEmpty()) {
            miraiComposeRepository.botList.add(null)
        }
        _botIndex = botList.lastIndex
    }

    fun onMenuToCurrentBot() = onRouteBot()

    fun onMenuToSpecificBot(index: Int) {
        _botIndex = index
        onRouteBot()
    }

    //  分别对于SideColumn的五个index
    fun onRouteBot() {
        _navigationIndex = 0

        router.push(Config.BotR(currentBot))
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
    Row(Modifier.fillMaxSize()) {
        SideColumn(navHost)
        Children(
            navHost.state, /*crossfade()*/
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
            navHost.botList,
            navHost.currentBot,
            onMenuItemSelected = navHost::onMenuToSpecificBot,
            onNewItemButtonSelected = navHost::onMenuAddNewBot,
            onLogoutButtonSelected = navHost::onMenuLogOutBot,
            onClick = navHost::onMenuToCurrentBot
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
}

@Composable
private fun AvatarWithMenu(
    composeBotList: List<Bot?>,
    currentBot: Bot?,
    onMenuItemSelected: (index: Int) -> Unit,
    onNewItemButtonSelected: () -> Unit,
    onLogoutButtonSelected: () -> Unit,
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
                .pointerInput(Unit) {
                    onRightClick {
                        isExpand = !isExpand
                    }
                }
                .fillMaxWidth()
                .requiredHeight(80.dp)
        ) {
            currentBot?.let {
                BotItem(currentBot)
            } ?: Text("No item")
        }

        DropdownMenu(isExpand, onDismissRequest = { isExpand = !isExpand }) {
            DropdownMenuItem(onClick = {
                onLogoutButtonSelected()
                isExpand = !isExpand
            }) {
                Text("Logout")
            }

            DropdownMenuItem(onClick = {
                onNewItemButtonSelected()
                isExpand = !isExpand
            }) {
                Text("Add")
            }

            itemsWithIndexed(composeBotList) { item, index ->
                DropdownMenuItem(onClick = {
                    onMenuItemSelected(index)
                    isExpand = !isExpand
                }) {
                    item?.let { BotItem(it) } ?: Text("Empty bot")
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
    bot: Bot,
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

val Bot.avatar
    get() = ImageBitmap(200, 200)

//SkiaImageDecode(
//Mirai.Http.get(this.avatarUrl) {
//    header("Connection", "close")
//}
//)
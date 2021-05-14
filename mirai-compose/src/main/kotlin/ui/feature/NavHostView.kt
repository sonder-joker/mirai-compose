package com.youngerhousea.miraicompose.ui.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.console.ComposeLog
import com.youngerhousea.miraicompose.future.inject
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.model.toComposeBot
import com.youngerhousea.miraicompose.theme.ComposeSetting
import com.youngerhousea.miraicompose.theme.R
import com.youngerhousea.miraicompose.ui.feature.about.About
import com.youngerhousea.miraicompose.ui.feature.about.AboutUi
import com.youngerhousea.miraicompose.ui.feature.bot.Login
import com.youngerhousea.miraicompose.ui.feature.bot.LoginUi
import com.youngerhousea.miraicompose.ui.feature.bot.Message
import com.youngerhousea.miraicompose.ui.feature.bot.MessageUi
import com.youngerhousea.miraicompose.ui.feature.log.ConsoleLog
import com.youngerhousea.miraicompose.ui.feature.log.ConsoleLogUi
import com.youngerhousea.miraicompose.ui.feature.plugin.Plugins
import com.youngerhousea.miraicompose.ui.feature.plugin.PluginsUi
import com.youngerhousea.miraicompose.ui.feature.setting.Setting
import com.youngerhousea.miraicompose.ui.feature.setting.SettingUi
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent
import com.youngerhousea.miraicompose.utils.items
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.MiraiConsole
import org.koin.core.qualifier.named

/**
 * 主界面
 *
 * @property navigationIndex 目前导航所指向的TabView
 * @property botList 目前的Console所登录的机器人List
 * @property currentBot 目前的显示的机器人
 *
 * @see [Login]
 * @see [MessageUi]
 * @see [Setting]
 * @see [About]
 * @see [ConsoleLog]
 * @see [Plugins]
 */
class NavHost(
    component: ComponentContext,
) : ComponentContext by component {
    private var _navigationIndex by mutableStateOf(0)

    private val _botList: MutableList<ComposeBot> by inject(named("ComposeBot"))

    private var _currentBot by mutableStateOf(_botList.firstOrNull())

    private val router = router<Configuration, Component>(
        initialConfiguration = Configuration.Login,
        handleBackButton = true,
        key = "NavHost",
        childFactory = { config, componentContext ->
            when (config) {
                is Configuration.Login ->
                    Login(componentContext, onLoginSuccess = ::onLoginSuccess).asComponent { LoginUi(it) }
                is Configuration.Message ->
                    Message(componentContext, botList).asComponent { MessageUi(it) }
                is Configuration.Plugin ->
                    Plugins(componentContext).asComponent { PluginsUi(it) }
                is Configuration.Setting ->
                    Setting(
                        componentContext,
                        ComposeSetting.AppTheme
                    ).asComponent { SettingUi(it) }
                is Configuration.ConsoleLog ->
                    ConsoleLog(
                        componentContext,
                        ComposeLog.logStorage,
                        MiraiConsole.mainLogger
                    ).asComponent { ConsoleLogUi(it) }
                is Configuration.About ->
                    About(componentContext).asComponent { AboutUi(it) }
            }
        }
    )

    val navigationIndex get() = _navigationIndex

    val botList: List<ComposeBot> get() = _botList

    val currentBot get() = _currentBot

    val state get() = router.state

    // 当机器人登录成功
    private fun onLoginSuccess(bot: Bot) {
        val composeBot = bot.toComposeBot()
        _currentBot = composeBot
        _botList.add(composeBot)
        router.push(Configuration.Message)
    }

    // 登录机器人
    fun addNewBot() {
        router.push(Configuration.Login)
    }

    fun onRouteToSpecificBot(bot: ComposeBot) {
        _currentBot = bot
    }

    fun onRouteMessage() {
        _navigationIndex = 0
        router.push(Configuration.Message)
    }

    fun onRoutePlugin() {
        _navigationIndex = 1
        router.push(Configuration.Plugin)
    }

    fun onRouteSetting() {
        _navigationIndex = 2
        router.push(Configuration.Setting)
    }

    fun onRouteLog() {
        _navigationIndex = 3
        router.push(Configuration.ConsoleLog)
    }

    fun onRouteAbout() {
        _navigationIndex = 4
        router.push(Configuration.About)
    }

    sealed class Configuration : Parcelable {
        object Message : Configuration()
        object Login : Configuration()
        object Setting : Configuration()
        object About : Configuration()
        object ConsoleLog : Configuration()
        object Plugin : Configuration()
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun NavHostUi(navHost: NavHost) {
    val height = 80.dp

    Row(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .width(160.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AvatarWithMenu(
                composeBotList = navHost.botList,
                onBoxClick = {
//                    if (navHost.currentBot != null)
//                        navHost.onRouteMessage()
//                    else
                        navHost.addNewBot()
                },
                onMenuBotSelected = navHost::onRouteToSpecificBot,
                onNewBotButtonSelected = navHost::addNewBot,
                modifier = Modifier.height(height),
            ) {
                BotItem(navHost.currentBot)
            }
//            SideTab(
//                onClick = navHost::onRouteMessage,
//                modifier = Modifier.height(height),
//            ) {
//                Icon(Icons.Outlined.Message, null)
//                Text(R.String.sideRowFirst, maxLines = 1)
//            }
            SideTab(
                onClick = navHost::onRoutePlugin,
                modifier = Modifier.height(height)
            ) {
                Icon(Icons.Outlined.Extension, null)
                Text(R.String.sideRowSecond)
            }
            SideTab(
                onClick = navHost::onRouteSetting,
                modifier = Modifier.height(height)
            ) {
                Icon(Icons.Outlined.Settings, null, modifier = Modifier)
                Text(R.String.sideRowThird)
            }
            SideTab(
                onClick = navHost::onRouteLog,
                modifier = Modifier.height(height)
            ) {
                Icon(Icons.Default.Notes, null)
                Text(R.String.sideRowFour, maxLines = 1)
            }
            SideTab(
                onClick = navHost::onRouteAbout,
                modifier = Modifier.height(height)
            ) {
                Icon(Icons.Default.Forum, null)
                Text(R.String.sideRowFive)
            }
        }

        Surface(color = Color(0xfffafafa)) {
            Children(
                navHost.state, crossfade()
            ) { child ->
                child.instance()
            }
        }
    }
}


@Composable
private fun AvatarWithMenu(
    composeBotList: List<ComposeBot>,
    onBoxClick: () -> Unit,
    onMenuBotSelected: (bot: ComposeBot) -> Unit,
    onNewBotButtonSelected: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    var menuExpand by remember { mutableStateOf(false) }

    Box(modifier) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onLongClick = { menuExpand = !menuExpand },
                    onClick = onBoxClick
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )

        DropdownMenu(menuExpand, onDismissRequest = { menuExpand = !menuExpand }) {
            DropdownMenuItem(onClick = { menuExpand = !menuExpand }) {
                Text(R.String.botMenuExit)
            }

            DropdownMenuItem(onClick = {
                onNewBotButtonSelected()
                menuExpand = !menuExpand
            }) {
                Text(R.String.botMenuAdd)
            }

            items(composeBotList) { item ->
                DropdownMenuItem(onClick = {
                    onMenuBotSelected(item)
                    menuExpand = !menuExpand
                }) {
                    BotItem(item)
                }
            }
        }
    }
}


@Composable
private fun SideTab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        modifier = modifier.fillMaxSize(),
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = MaterialTheme.colors.background),
        border = null,
        content = content
    )
}

@Composable
private fun BotItem(
    bot: ComposeBot?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .aspectRatio(2f)
            .clipToBounds(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.weight(1f).fillMaxHeight())
        Surface(
            modifier = Modifier
                .weight(3f, fill = false),
            shape = CircleShape,
            color = Color(0xff979595),
        ) {
            Image(bot?.avatar ?: ImageBitmap(200, 200), null)
        }
        Column(
            Modifier
                .weight(6f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(bot?.nick ?: "Login", fontWeight = FontWeight.Bold, maxLines = 1)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("${bot?.id ?: "Unknown"}", style = MaterialTheme.typography.body2)
            }
        }
    }
}

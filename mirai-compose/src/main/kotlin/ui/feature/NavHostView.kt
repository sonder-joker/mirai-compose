package com.youngerhousea.miraicompose.ui.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.*
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.future.inject
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.model.toComposeBot
import com.youngerhousea.miraicompose.theme.R
import com.youngerhousea.miraicompose.ui.feature.about.About
import com.youngerhousea.miraicompose.ui.feature.about.AboutUi
import com.youngerhousea.miraicompose.ui.feature.bot.Login
import com.youngerhousea.miraicompose.ui.feature.bot.LoginUi
import com.youngerhousea.miraicompose.ui.feature.bot.OnlineBot
import com.youngerhousea.miraicompose.ui.feature.bot.OnlineBotUi
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
import org.koin.core.qualifier.named

/**
 * 主界面
 *
 * @property navigationIndex 目前导航所指向的TabView
 * @property botList 目前的Console所登录的机器人List
 * @property currentBot 目前的显示的机器人
 *
 * @see [Login]
 * @see [OnlineBot]
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
                is Configuration.OnlineBot ->
                    OnlineBot(componentContext, config.bot).asComponent { OnlineBotUi(it) }
                is Configuration.Plugin ->
                    Plugins(componentContext).asComponent { PluginsUi(it) }
                is Configuration.Setting ->
                    Setting(componentContext).asComponent { SettingUi(it) }
                is Configuration.ConsoleLog ->
                    ConsoleLog(componentContext).asComponent { ConsoleLogUi(it) }
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
        onRouteToSpecificBot(composeBot)
    }

    // 登录机器人
    fun addNewBot() {
        _navigationIndex = 0
        router.push(Configuration.Login)
    }

    fun onRouteToSpecificBot(bot: ComposeBot) {
        router.push(Configuration.OnlineBot(bot))
    }

    fun onRouteBot() {
        _navigationIndex = 0
        currentBot?.let { router.push(Configuration.OnlineBot(it)) } ?: router.push(Configuration.Login)
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
        class OnlineBot(val bot: ComposeBot) : Configuration()
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
        onMenuBotSelected = navHost::onRouteToSpecificBot,
        onNewBotButtonSelected = navHost::addNewBot,
    )
    SelectEdgeText(
        R.String.sideRowFirst,
        isWishWindow = navHost.navigationIndex == 0,
        onClick = navHost::onRouteBot
    )
    SelectEdgeText(
        R.String.sideRowSecond,
        isWishWindow = navHost.navigationIndex == 1,
        onClick = navHost::onRoutePlugin
    )
    SelectEdgeText(
        R.String.sideRowThird,
        isWishWindow = navHost.navigationIndex == 2,
        onClick = navHost::onRouteSetting
    )
    SelectEdgeText(
        R.String.sideRowFour,
        isWishWindow = navHost.navigationIndex == 3,
        onClick = navHost::onRouteLog
    )
    SelectEdgeText(
        R.String.sideRowFive,
        isWishWindow = navHost.navigationIndex == 4,
        onClick = navHost::onRouteAbout
    )
}

@Composable
private fun AvatarWithMenu(
    composeBotList: List<ComposeBot>,
    currentBot: ComposeBot?,
    onMenuBotSelected: (bot: ComposeBot) -> Unit,
    onNewBotButtonSelected: () -> Unit,
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
            } ?: Text(R.String.botMenuEmpty)
        }

        DropdownMenu(isExpand, onDismissRequest = { isExpand = !isExpand }) {
            DropdownMenuItem(onClick = { isExpand = !isExpand }) {
                Text(R.String.botMenuExit)
            }

            DropdownMenuItem(onClick = {
                onNewBotButtonSelected()
                isExpand = !isExpand
            }) {
                Text(R.String.botMenuAdd)
            }

            items(composeBotList) { item ->
                DropdownMenuItem(onClick = {
                    onMenuBotSelected(item)
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

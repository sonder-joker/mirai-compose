package com.youngerhousea.miraicompose.app.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.youngerhousea.miraicompose.app.utils.R
import com.youngerhousea.miraicompose.core.component.NavHost
import com.youngerhousea.miraicompose.core.component.about.About
import com.youngerhousea.miraicompose.core.component.bot.Login
import com.youngerhousea.miraicompose.core.component.log.ConsoleLog
import com.youngerhousea.miraicompose.core.component.message.Message
import com.youngerhousea.miraicompose.core.component.plugin.Plugins
import com.youngerhousea.miraicompose.core.component.setting.Setting
import com.youngerhousea.miraicompose.core.console.ComposeBot
import com.youngerhousea.miraicompose.app.future.splitpane.ExperimentalSplitPaneApi
import com.youngerhousea.miraicompose.app.future.splitpane.HorizontalSplitPane
import com.youngerhousea.miraicompose.app.future.splitpane.rememberSplitPaneState
import com.youngerhousea.miraicompose.app.ui.about.AboutUi
import com.youngerhousea.miraicompose.app.ui.bot.LoginUi
import com.youngerhousea.miraicompose.app.ui.log.ConsoleLogUi
import com.youngerhousea.miraicompose.app.ui.message.MessageUi
import com.youngerhousea.miraicompose.app.ui.plugin.PluginsUi
import com.youngerhousea.miraicompose.app.ui.setting.SettingUi
import com.youngerhousea.miraicompose.app.utils.SkiaImageDecode
import com.youngerhousea.miraicompose.app.utils.items

@OptIn(ExperimentalDecomposeApi::class, ExperimentalSplitPaneApi::class)
@Composable
fun NavHostUi(navHost: NavHost) {
    val height = 80.dp

    val splitterState = rememberSplitPaneState()

    var navigationIndex by remember { mutableStateOf(0) }

    HorizontalSplitPane(splitPaneState = splitterState) {
        first(160.dp) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
            ) {
                AvatarWithMenu(
                    composeBotList = navHost.botList,
                    onBoxClick = {
                        navigationIndex = 5
                        if (navHost.currentBot != null)
                            navHost.onRouteMessage()
                        else
                            navHost.addNewBot()
                    },
                    onMenuBotSelected = navHost::onRouteToSpecificBot,
                    onNewBotButtonSelected = navHost::addNewBot,
                    modifier = Modifier.height(height),
                ) {
                    BotItem(navHost.currentBot)
                }
                SideTab(
                    onClick = {
                        navHost.onRouteMessage()
                        navigationIndex = 0
                    },
                    modifier = Modifier.height(height),
                    isWish = navigationIndex == 0
                ) {
                    Icon(Icons.Outlined.Message, null)
                    Text(R.String.sideRowFirst)
                }
                SideTab(
                    onClick = {
                        navHost.onRoutePlugin()
                        navigationIndex = 1
                    },
                    isWish = navigationIndex == 1,
                    modifier = Modifier.height(height),
                ) {
                    Icon(Icons.Outlined.Extension, null)
                    Text(R.String.sideRowSecond)
                }
                SideTab(
                    onClick = {
                        navHost.onRouteSetting()
                        navigationIndex = 2
                    },
                    modifier = Modifier.height(height),
                    isWish = navigationIndex == 2
                ) {
                    Icon(Icons.Outlined.Settings, null)
                    Text(R.String.sideRowThird)
                }
                SideTab(
                    onClick = {
                        navHost.onRouteLog()
                        navigationIndex = 3
                    },
                    modifier = Modifier.height(height),
                    isWish = navigationIndex == 3
                ) {
                    Icon(Icons.Outlined.Notes, null)
                    Text(R.String.sideRowFour, maxLines = 1)
                }
                SideTab(
                    onClick = navHost::onRouteAbout,
                    modifier = Modifier.height(height),
                    isWish = navigationIndex == 4
                ) {
                    Icon(Icons.Outlined.Forum, null)
                    Text(R.String.sideRowFive)
                }
            }
        }

        splitter {
            visiblePart {
                Box(
                    Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colors.background)
                )
            }
            handle {
                Box(
                    Modifier
                        .markAsHandle()
                        .background(SolidColor(Color.Gray), alpha = 0.5f)
                        .width(1.dp)
                        .fillMaxHeight()
                )
            }
        }

        second(500.dp) {
            Box(Modifier.fillMaxSize().clipToBounds()) {
                Children(
                    navHost.state, crossfade()
                ) { child ->
                    when (val ch = child.instance) {
                        is Login -> LoginUi(ch)
                        is ConsoleLog -> ConsoleLogUi(ch)
                        is About -> AboutUi(ch)
                        is Setting -> SettingUi(ch)
                        is Plugins -> PluginsUi(ch)
                        is Message -> MessageUi(ch)
                    }
                }
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
            horizontalArrangement = Arrangement.Start,
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
    isWish: Boolean,
    content: @Composable RowScope.() -> Unit,
) {
    val color by animateColorAsState(if (isWish) Color.Green else MaterialTheme.colors.primary)

    CompositionLocalProvider(LocalContentColor provides color) {
        Row(
            modifier = modifier.fillMaxSize().clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.aligned(Alignment.CenterHorizontally),
            content = content
        )
    }
}

@Composable
private fun BotItem(
    bot: ComposeBot?,
    modifier: Modifier = Modifier,
) {
    val avatar = remember(bot) { bot?.let { SkiaImageDecode(it.avatar) } ?: ImageBitmap(200, 200) }

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
            Image(avatar, null)
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

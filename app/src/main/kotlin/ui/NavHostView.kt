package com.youngerhousea.miraicompose.app.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.youngerhousea.miraicompose.app.future.splitpane.ExperimentalSplitPaneApi
import com.youngerhousea.miraicompose.app.future.splitpane.HorizontalSplitPane
import com.youngerhousea.miraicompose.app.future.splitpane.rememberSplitPaneState
import com.youngerhousea.miraicompose.app.ui.about.AboutUi
import com.youngerhousea.miraicompose.app.ui.bot.LoginUi
import com.youngerhousea.miraicompose.app.ui.log.ConsoleLogUi
import com.youngerhousea.miraicompose.app.ui.message.MessageUi
import com.youngerhousea.miraicompose.app.ui.plugin.PluginsUi
import com.youngerhousea.miraicompose.app.ui.setting.SettingUi
import com.youngerhousea.miraicompose.app.utils.R
import com.youngerhousea.miraicompose.app.utils.SkiaImageDecode
import com.youngerhousea.miraicompose.core.component.AvatarMenu
import com.youngerhousea.miraicompose.core.component.BotItem
import com.youngerhousea.miraicompose.core.component.NavHost
import com.youngerhousea.miraicompose.core.utils.activeInstance

private val RailTabHeight = 80.dp


@OptIn(ExperimentalDecomposeApi::class, ExperimentalSplitPaneApi::class)
@Composable
fun NavHostUi(navHost: NavHost) {
    val splitterState = rememberSplitPaneState()

    val router by navHost.state.subscribeAsState()

    HorizontalSplitPane(splitPaneState = splitterState) {
        first(160.dp) {
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
            ) {
                AvatarWithMenu(navHost.avatarMenu)
                RailTab(
                    onClick = navHost::onRouteMessage,
                    selected = router.activeInstance is NavHost.Child.CMessage
                ) {
                    Icon(Icons.Outlined.Message, null)
                    Text(R.String.sideRowFirst)
                }
                RailTab(
                    onClick = navHost::onRoutePlugin,
                    selected = router.activeInstance is NavHost.Child.CPlugins,
                ) {
                    Icon(Icons.Outlined.Extension, null)
                    Text(R.String.sideRowSecond)
                }
                RailTab(
                    onClick = navHost::onRouteSetting,
                    selected = router.activeInstance is NavHost.Child.CSetting
                ) {
                    Icon(Icons.Outlined.Settings, null)
                    Text(R.String.sideRowThird)
                }
                RailTab(
                    onClick = navHost::onRouteLog,
                    selected = router.activeInstance is NavHost.Child.CConsoleLog
                ) {
                    Icon(Icons.Outlined.Notes, null)
                    Text(R.String.sideRowFour, maxLines = 1)
                }
                RailTab(
                    onClick = navHost::onRouteAbout,
                    selected = router.activeInstance is NavHost.Child.CAbout
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
//                        .width(10.dp)
//                        .fillMaxHeight()
//                        .background(Color.Gray)
                )
            }
            handle {
                Box(
                    Modifier
//                        .markAsHandle()
//                        .background(SolidColor(Color.Gray), alpha = 0.5f)
//                        .width(10.dp)
//                        .fillMaxHeight()
                )
            }
        }

        second(500.dp) {
            Box(Modifier.fillMaxSize().clipToBounds()) {
                Children(router, crossfade()) { child ->
                    when (val ch = child.instance) {
                        is NavHost.Child.CLogin -> LoginUi(ch.login)
                        is NavHost.Child.CConsoleLog -> ConsoleLogUi(ch.log)
                        is NavHost.Child.CAbout -> AboutUi(ch.about)
                        is NavHost.Child.CSetting -> SettingUi(ch.setting)
                        is NavHost.Child.CPlugins -> PluginsUi(ch.plugins)
                        is NavHost.Child.CMessage -> MessageUi(ch.message)
                    }
                }
            }
        }
    }
}

@Composable
fun AvatarWithMenu(
    avatarMenu: AvatarMenu,
    modifier: Modifier = Modifier,
) {
    val model by avatarMenu.model.collectAsState()

    Box(modifier.height(RailTabHeight).fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onLongClick = avatarMenu::openExpandMenu,
                    onClick = avatarMenu::onAvatarBoxClick
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BotItem(model.currentBot)
        }

        DropdownMenu(model.isExpand, onDismissRequest = avatarMenu::dismissExpandMenu) {
            DropdownMenuItem(onClick = avatarMenu::dismissExpandMenu) {
                Text(R.String.botMenuExit)
            }

            DropdownMenuItem(onClick = avatarMenu::addNewBot) {
                Text(R.String.botMenuAdd)
            }

            model.botList.forEach { item ->
                DropdownMenuItem(onClick = {
                    avatarMenu.onItemClick(item)
                }) {
                    BotItem(item)
                }
            }
        }
    }
}

@Composable
fun BotItem(
    botItem: BotItem?,
    modifier: Modifier = Modifier
) {

    val avatarByteArray = botItem?.avatar?.collectAsState()

    val avatar by derivedStateOf {
        avatarByteArray?.value?.let { SkiaImageDecode(it) } ?: ImageBitmap(200, 200)
    }

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
            Text(botItem?.bot?.run {
                try {
                    nick
                } catch (e: UninitializedPropertyAccessException) {
                    "Unknown"
                }
            } ?: "Login", fontWeight = FontWeight.Bold, maxLines = 1)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("${botItem?.bot?.id ?: "Unknown"}", style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Composable
private fun RailTab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    selected: Boolean,
    content: @Composable RowScope.() -> Unit,
) {
    val color by animateColorAsState(if (selected) Color.Green else MaterialTheme.colors.primary)

    CompositionLocalProvider(LocalContentColor provides color) {
        Row(
            modifier = modifier.height(RailTabHeight).fillMaxWidth().clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            content = content
        )
    }
}




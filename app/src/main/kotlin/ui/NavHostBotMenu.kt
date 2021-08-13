package com.youngerhousea.mirai.compose.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youngerhousea.mirai.compose.console.viewModel
import com.youngerhousea.mirai.compose.resource.R
import com.youngerhousea.mirai.compose.ui.login.LoginDialog
import com.youngerhousea.mirai.compose.viewmodel.Event
import com.youngerhousea.mirai.compose.viewmodel.HostRoute
import com.youngerhousea.mirai.compose.viewmodel.HostViewModel
import net.mamoe.mirai.Bot

@Composable
fun NavHostFirstBotMenu(
    hostViewModel: HostViewModel = viewModel { HostViewModel() }
) {
    val state by hostViewModel.hostState

    LoginDialog(
        visible = state.loginDialogIsExpand,
        onCloseRequest = { hostViewModel.dispatch(Event.CloseLoginDialog) }
    )

    NavHostFirstBotMenuContent(
        isExpand = state.menuIsExpand,
        botList = state.botList,
        currentBot = state.currentBot,
        onAvatarBoxClick = { hostViewModel.dispatch(Event.OpenMenu) },
        dismissExpandMenu = { hostViewModel.dispatch(Event.CloseMenu) },
        onAddNewBotButtonClick = { hostViewModel.dispatch(Event.OpenLoginDialog) },
        onBotItemClick = { hostViewModel.dispatch(HostRoute.BotMessage(it))}
    )
}

@Composable
fun NavHostFirstBotMenuContent(
    isExpand: Boolean,
    botList: List<Bot>,
    currentBot: Bot?,
    onAvatarBoxClick: () -> Unit,
    dismissExpandMenu: () -> Unit,
    onAddNewBotButtonClick: () -> Unit,
    onBotItemClick: (Bot) -> Unit
) {
    Box(Modifier.height(80.dp).fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onAvatarBoxClick),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (currentBot == null)
                EmptyBotItem()
            else
                BotItem(currentBot)
        }

        DropdownMenu(isExpand, onDismissRequest = dismissExpandMenu) {
            DropdownMenuItem(onClick = dismissExpandMenu) {
                Text(R.String.BotMenuExit)
            }

            DropdownMenuItem(onClick = onAddNewBotButtonClick) {
                Text(R.String.BotMenuAdd)
            }

            botList.forEach { bot ->
                DropdownMenuItem(onClick = {
                    onBotItemClick(bot)
                }) {
                    BotItem(bot)
                }
            }
        }
    }
}

@Preview
@Composable
fun NavHostFirstBotMenuPreview() {
    NavHostFirstBotMenuContent(
        isExpand = false,
        botList = listOf(EmptyBot, EmptyBot),
        currentBot = null,
        onAvatarBoxClick = {},
        dismissExpandMenu = {},
        onAddNewBotButtonClick = {},
        onBotItemClick = {}
    )
}
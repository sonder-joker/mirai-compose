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
import com.youngerhousea.mirai.compose.viewmodel.*
import net.mamoe.mirai.Bot

@Composable
fun NavHostFirstBotMenu(
    hostViewModel: Host = viewModel { HostViewModel() },
    loginViewModel: Login = viewModel { LoginViewModel() }
) {
    val state by hostViewModel.hostState

    NavHostFirstBotMenuContent(
        isExpand = state.menuIsExpand,
        botList = state.botList,
        currentBot = state.currentBot,
        onAvatarBoxClick = { hostViewModel.dispatch(HostAction.OpenMenu) },
        dismissExpandMenu = { hostViewModel.dispatch(HostAction.CloseMenu) },
        onAddNewBotButtonClick = {
            loginViewModel.dispatch(LoginAction.Open)
        },
        onBotItemClick = { hostViewModel.dispatch(HostRoute.BotMessage(it)) }
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
package com.youngerhousea.mirai.compose.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youngerhousea.mirai.compose.console.viewModel
import com.youngerhousea.mirai.compose.resource.R
import com.youngerhousea.mirai.compose.viewmodel.HostViewModel
import net.mamoe.mirai.Bot

@Composable
fun NavHostFirstBotMenu(
    hostViewModel: HostViewModel = viewModel { HostViewModel() }
) {
    NavHostFirstBotMenuContent(
        isExpand = hostViewModel.isExpand.value,
        botList = hostViewModel.botList,
        currentBot = hostViewModel.currentBot.value,
        openExpandMenu = hostViewModel::openExpandMenu,
        onAvatarBoxClick = {},
        dismissExpandMenu = hostViewModel::dismissExpandMenu,
        addNewBot = {},
        onBotClick = {}
    )
}

@Composable
fun NavHostFirstBotMenuContent(
    isExpand: Boolean,
    botList: List<Bot>,
    currentBot: Bot?,
    openExpandMenu: () -> Unit,
    onAvatarBoxClick: () -> Unit,
    dismissExpandMenu: () -> Unit,
    addNewBot: () -> Unit,
    onBotClick: (Bot) -> Unit
) {
    Box(Modifier.height(80.dp).fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onLongClick = openExpandMenu,
                    onClick = onAvatarBoxClick
                ),
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

            DropdownMenuItem(onClick = addNewBot) {
                Text(R.String.BotMenuAdd)
            }

            botList.forEach { bot ->
                DropdownMenuItem(onClick = {
                    onBotClick(bot)
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
        openExpandMenu = {},
        dismissExpandMenu = {},
        addNewBot = {},
        onBotClick = {}
    )
}
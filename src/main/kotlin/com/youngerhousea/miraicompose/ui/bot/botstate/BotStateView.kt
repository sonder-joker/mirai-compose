package com.youngerhousea.miraicompose.ui.bot.botstate

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.theme.AppTheme
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.command.ConsoleCommandSender

@Composable
fun BotStateView(model: ComposeBot) = Box(
    Modifier
        .fillMaxSize(),
    contentAlignment = Alignment.BottomCenter
) {
    Column {
        LogBox(
            model,
            Modifier
                .weight(8f)
                .padding(horizontal = 40.dp)
                .fillMaxWidth()
        )
        CommandSendBox(
            model,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
        )
    }
}

@Composable
private fun LogBox(bot: ComposeBot, modifier: Modifier = Modifier) {
    Box(modifier) {
        LazyColumn(
            Modifier
                .fillMaxWidth()
        ) {
            items(bot.messages) {
                Text(
                    "${it.sender.nick} in ${it.time} say: ${it.message}",
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                )
            }
        }
    }
}

@Composable
private fun CommandSendBox(bot: ComposeBot, modifier: Modifier = Modifier) {
    Row(modifier) {
        TextField(
            bot.currentCommand,
            onValueChange = {
                bot.currentCommand = it
            },
            modifier = Modifier
                .weight(13f),
            singleLine = true,
        )

        Spacer(
            Modifier.weight(1f)
        )

        FloatingActionButton(
            {
                MiraiConsole.launch {
                    ConsoleCommandSender.sendMessage(bot.currentCommand)
                }
            }, modifier = Modifier
                .weight(2f),
            backgroundColor = AppTheme.colors.backgroundDark
        ) {
            Text("发送")
        }
    }
}

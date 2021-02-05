package com.youngerhousea.miraicompose.ui.bot.botstate

import androidx.compose.runtime.Composable
import com.youngerhousea.miraicompose.model.BotState
import com.youngerhousea.miraicompose.model.ComposeBot
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.MiraiConsole

@Composable
fun BotChooseWindow(model: ComposeBot) =
    when (model.state) {
        BotState.None -> {
            BotLoginView(model.loginWindowState) {
                MiraiConsole.launch {
                    model.login()
                }
            }
        }
        BotState.Loading -> {
            BotLoadingView()
        }
        BotState.Login -> {
            BotStateView(model)
        }
    }


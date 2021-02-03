package com.youngerhousea.miraicompose.ui.botwindows

import androidx.compose.runtime.Composable
import com.youngerhousea.miraicompose.model.BotState
import com.youngerhousea.miraicompose.model.ComposeBot
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.MiraiConsole

@Composable
fun BotWindow(model: ComposeBot) =
    when (model.state) {
        BotState.None -> {
            BotLoginWindow(model.loginWindowState) {
                MiraiConsole.launch {
                    model.login()
                }
            }

//            BotStateWindow(model)
        }
        BotState.Loading -> {
            BotLoadingWindow()
        }
        BotState.Login -> {
            BotStateWindow(model)
        }
    }


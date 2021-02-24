package com.youngerhousea.miraicompose.ui.bot.botstate

import androidx.compose.desktop.LocalAppWindow
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.model.BotState
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.model.LoginWindowState
import com.youngerhousea.miraicompose.ui.botstate.BotStateView
import kotlinx.coroutines.launch

@Composable
fun BotChooseWindow(model: ComposeBot) {
    val loginWindowState = remember(model) { LoginWindowState() }

    when (model.state) {
        BotState.None -> {
            BotLoginView(loginWindowState) {
                MiraiCompose.launch {
                    model.login(loginWindowState.account.text, loginWindowState.password.text)
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

}
package com.youngerhousea.miraicompose.ui.feature.bot.botstate

import androidx.compose.runtime.Composable
import com.youngerhousea.miraicompose.console.logs
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.ui.common.LogBox

@Composable
fun BotStateView(bot: ComposeBot) = /*Box(
    Modifier
        .fillMaxSize(),
    contentAlignment = Alignment.BottomCenter
) {*/
    LogBox(
        bot.logs,
    )
//}


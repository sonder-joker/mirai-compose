package com.youngerhousea.miraicompose.ui.feature.bot.botstate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.youngerhousea.miraicompose.console.logs
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.ui.common.LogBox

@Composable
fun BotStateView(bot: ComposeBot) = Box(
    Modifier
        .fillMaxSize(),
    contentAlignment = Alignment.TopCenter
) {
    LogBox(
        bot.logs,
    )
}


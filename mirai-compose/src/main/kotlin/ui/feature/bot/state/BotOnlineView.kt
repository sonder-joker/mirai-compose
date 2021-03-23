package com.youngerhousea.miraicompose.ui.feature.bot.state

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.console.MiraiComposeLogger.Companion.logs
import com.youngerhousea.miraicompose.ui.common.LogBox
import net.mamoe.mirai.Bot

class BotOnline(context: ComponentContext, val bot: Bot): ComponentContext by context {

}

@Composable
fun BotOnlineUi(botOnline: BotOnline) =
    LogBox(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp, vertical = 20.dp),
        botOnline.bot.logs,
    )


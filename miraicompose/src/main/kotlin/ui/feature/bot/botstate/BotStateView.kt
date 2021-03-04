package com.youngerhousea.miraicompose.ui.feature.bot.botstate

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.console.logs
import com.youngerhousea.miraicompose.ui.common.LogBox
import com.youngerhousea.miraicompose.utils.Component
import net.mamoe.mirai.Bot

class BotState(context: ComponentContext, val bot: Bot):Component, ComponentContext by context{
    @Composable
    override fun render() {
        BotStateView(bot)
    }

}

@Composable
fun BotStateView(bot: Bot) = /*Box(
    Modifier
        .fillMaxSize(),
    contentAlignment = Alignment.BottomCenter
) {*/
    LogBox(
        bot.logs,
    )
//}


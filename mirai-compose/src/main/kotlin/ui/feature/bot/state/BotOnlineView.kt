package com.youngerhousea.miraicompose.ui.feature.bot.state

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.console.MiraiComposeLogger.Companion.logs
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.ui.common.LogBox
import com.youngerhousea.miraicompose.ui.common.VerticalSplittableSimple
import com.youngerhousea.miraicompose.ui.feature.bot.EventListView
import com.youngerhousea.miraicompose.ui.feature.bot.TopView

class BotOnline(context: ComponentContext, val bot: ComposeBot) : ComponentContext by context

@Composable
fun BotOnlineUi(botOnline: BotOnline) {
    VerticalSplittableSimple(
        resizablePanelContent = {
            Column {
                TopView(
                    Modifier.padding(8.dp)
                )
                EventListView(botOnline.bot.events)
            }
        }, rightContent = {
            LogBox(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 40.dp, vertical = 20.dp),
                botOnline.bot.toBot().logs,
            )
        })
}



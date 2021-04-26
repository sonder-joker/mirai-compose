package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.ui.common.VerticalSplittableSimple
import kotlinx.coroutines.InternalCoroutinesApi
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import net.mamoe.mirai.event.events.BotLeaveEvent
import net.mamoe.mirai.event.events.MessageEvent

/**
 * 在线bot的页面
 *
 */
class OnlineBot(
    componentContext: ComponentContext,
    val bot: ComposeBot
) : ComponentContext by componentContext

@Composable
fun OnlineBotUi(onlineBot: OnlineBot) {
    VerticalSplittableSimple(
        resizablePanelContent = {
            Column {
                TopView(
                    Modifier.padding(8.dp)
                )
                EventListView(onlineBot.bot.eventList)
            }
        }, rightContent = {
            Column {

            }
        })
}

@Composable
private fun TopView(modifier: Modifier) =
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Events",
            color = LocalContentColor.current.copy(alpha = 0.60f),
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }

@OptIn(InternalCoroutinesApi::class)
@Composable
private fun EventListView(event: List<BotEvent>) {
    LazyColumn {
        items(event) { botEvent ->
            Card(backgroundColor = MaterialTheme.colors.background) {
                Text(ParseEventString(botEvent))
            }
        }
    }
}

@Composable
private fun ParseEventString(botEvent: BotEvent): String {
    return when (botEvent) {
        is BotInvitedJoinGroupRequestEvent -> "BotInvitedJoinGroupRequestEvent"
        is BotLeaveEvent -> "BotLeaveEvent"
        is MessageEvent -> "MessageEvent"
        else -> "Unknown Event"
    }
}


inline val Bot.stringId get() = this.id.toString()



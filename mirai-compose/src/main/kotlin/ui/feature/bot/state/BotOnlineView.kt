package com.youngerhousea.miraicompose.ui.feature.bot.state

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.future.inject
import com.youngerhousea.miraicompose.ui.common.LogBox
import com.youngerhousea.miraicompose.ui.common.VerticalSplittableSimple
import kotlinx.coroutines.InternalCoroutinesApi
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import net.mamoe.mirai.event.events.BotLeaveEvent

class BotOnline(
    context: ComponentContext,
    private val bot: Bot
) : ComponentContext by context {
    private val _logs by inject<List<AnnotatedString>>()

    val logs get() = _logs.filter { it.text.contains(bot.stringId) }

    private val _eventList = mutableStateListOf<BotEvent>()

    init {
        bot.eventChannel.subscribeAlways<BotEvent> {
            _eventList.add(it)
        }
    }

    val eventList: List<BotEvent> get() = _eventList
}

@Composable
fun BotOnlineUi(botOnline: BotOnline) {
    VerticalSplittableSimple(
        resizablePanelContent = {
            Column {
                TopView(
                    Modifier.padding(8.dp)
                )
                EventListView(botOnline.eventList)
            }
        }, rightContent = {
            LogBox(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 40.dp, vertical = 20.dp),
                botOnline.logs,
            )
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
            Text(ParseEventString(botEvent), color = Color.Red)
        }
    }
}

@Composable
private fun ParseEventString(botEvent: BotEvent): String {
    return when (botEvent) {
        is BotInvitedJoinGroupRequestEvent -> "BotInvitedJoinGroupRequestEvent"
        is BotLeaveEvent -> "BotLeaveEvent"
        else -> "Unknown Event"
    }
}


inline val Bot.stringId get() = this.id.toString()



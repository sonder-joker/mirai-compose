package com.youngerhousea.miraicompose.ui.feature.bot.state

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.ui.common.VerticalSplittableSimple
import com.youngerhousea.miraicompose.utils.ComponentChildScope
import kotlinx.coroutines.InternalCoroutinesApi
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import net.mamoe.mirai.event.events.BotLeaveEvent

class BotOnline(context: ComponentContext, val bot: Bot) : ComponentContext by context {
    val scope = ComponentChildScope()

    init {
        bot.eventChannel.parentScope(scope)
    }
}

@Composable
fun BotOnlineUi(botOnline: BotOnline) {
    VerticalSplittableSimple(
        resizablePanelContent = {
            Column {
                TopView(
                    Modifier.padding(8.dp)
                )
                EventListView(botOnline.bot.eventChannel)
            }
        }, rightContent = {
//            LogBox(
//                Modifier
//                    .fillMaxSize()
//                    .padding(horizontal = 40.dp, vertical = 20.dp),
//                botOnline.bot.logg,
//            )
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
private fun EventListView(event: EventChannel<BotEvent>) {
    val list = mutableStateListOf<BotEvent>()
    SideEffect {
        event.subscribeAlways<BotEvent> {
            list.add(this)
        }
    }

    LazyColumn {
        items(list) { botEvent ->
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




package com.youngerhousea.miraicompose.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import com.youngerhousea.miraicompose.utils.SkiaImageDecode
import io.ktor.client.request.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.Mirai
import net.mamoe.mirai.event.events.BotEvent

interface ComposeBot : Bot {
    val avatar: ImageBitmap

    val eventList: List<BotEvent>
}

fun Bot.toComposeBot(): ComposeBot = ComposeBotImpl(this)

@OptIn(ExperimentalCoroutinesApi::class)
internal class ComposeBotImpl(bot: Bot) : Bot by bot, ComposeBot {
    private var _avatar by mutableStateOf(ImageBitmap(200, 200))

    private val _eventList = mutableStateListOf<BotEvent>()

    override val eventList: List<BotEvent> get() = _eventList

    init {
        launch {
            _avatar = SkiaImageDecode(
                Mirai.Http.get(avatarUrl) {
                    header("Connection", "close")
                }
            )
        }
        launch {
            bot.eventChannel.asChannel().receiveAsFlow().collect {
                _eventList.add(it)
            }
        }

    }

    override val avatar get() = _avatar
}

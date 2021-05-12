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
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.subscribe

interface ComposeBot : Bot {
    val avatar: ImageBitmap

    val messageSpeed: Int
}

fun Bot.toComposeBot(): ComposeBot = ComposeBotImpl(this)

@OptIn(ExperimentalCoroutinesApi::class)
internal class ComposeBotImpl(bot: Bot) : Bot by bot, ComposeBot {
    private var _avatar by mutableStateOf(ImageBitmap(200, 200))

    private var _messageSpeed by mutableStateOf(0)

    init {
        launch {
            _avatar = SkiaImageDecode(
                Mirai.Http.get(avatarUrl) {
                    header("Connection", "close")
                }
            )
        }
        launch {
            bot.eventChannel.subscribeAlways<MessageEvent> {
                _messageSpeed++
            }
        }

    }

    override val avatar get() = _avatar

    override val messageSpeed: Int get() = _messageSpeed
}

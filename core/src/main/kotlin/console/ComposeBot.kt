package com.youngerhousea.miraicompose.core.console

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.reduce
import io.ktor.client.request.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.Mirai
import net.mamoe.mirai.event.events.MessageEvent

interface ComposeBot : Bot {
    val avatar: ByteArray

    val messageSpeed: Int
}

fun Bot.toComposeBot(): ComposeBot = ComposeBotImpl(this)

@OptIn(ExperimentalCoroutinesApi::class)
internal class ComposeBotImpl(bot: Bot) : Bot by bot, ComposeBot {
    private var _messageSpeed = MutableValue(0)

    override var avatar = ByteArray(1)

    override val messageSpeed get() = _messageSpeed.value

    init {
        launch {
            avatar =
                Mirai.Http.get(avatarUrl) {
                    header("Connection", "close")
                }
            launch {
                bot.eventChannel.subscribeAlways<MessageEvent> {
                    _messageSpeed.value++
                }
            }
        }
    }
}

package com.youngerhousea.miraicompose.core.console

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
    override var avatar = ByteArray(100)

    override var messageSpeed = 0

    init {
        launch {
            avatar =
                Mirai.Http.get(avatarUrl) {
                    header("Connection", "close")
                }
            launch {
                bot.eventChannel.subscribeAlways<MessageEvent> {
                    messageSpeed++
                }
            }
        }
    }
}

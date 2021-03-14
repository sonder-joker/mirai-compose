package com.youngerhousea.miraicompose.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.youngerhousea.miraicompose.utils.getAvatarImage
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.supervisorJob
import kotlin.concurrent.timer
import kotlin.coroutines.suspendCoroutine


interface ComposeBot : Bot {

    // Mutable delegete
    val avatar: ImageBitmap

    val time: Long

    val messageCount: Long

    companion object {
        //only after longin
        operator fun invoke(bot: Bot): ComposeBot = ComposeBotImpl(bot)

        val instances: MutableList<ComposeBot> = mutableStateListOf()
    }
}

private class ComposeBotImpl(bot: Bot) : Bot by bot, ComposeBot {

    private var _avatar by mutableStateOf(ImageBitmap(200, 200))

    private var _messageCount by mutableStateOf(0L)

    private var _time by mutableStateOf(0L)

    override val avatar: ImageBitmap get() = _avatar

    override val messageCount: Long get() = _messageCount

    override val time: Long get() = _time

    override suspend fun login() {
        bot.login()
        loadResource()
    }

    init {
        ComposeBot.instances.add(this)
        if(bot.isOnline) {
            launch {
                loadResource()
            }
        }
        supervisorJob.invokeOnCompletion {
            ComposeBot.instances.remove(this)
        }

    }

    private suspend fun loadResource() {
        _avatar = bot.getAvatarImage()
        eventChannel.subscribeAlways<MessageEvent> {
            _messageCount++
        }

        while (true) {
            delay(1000)
            _time++
        }
    }
}


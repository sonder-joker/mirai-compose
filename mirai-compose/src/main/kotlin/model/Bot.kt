package com.youngerhousea.miraicompose.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import com.youngerhousea.miraicompose.utils.SkiaImageDecode
import io.ktor.client.request.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.Mirai
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.utils.BotConfiguration

interface ComposeBot {

    val avatar: ImageBitmap

    val time: Long

    val id: String

    val nick: String

    val state: State

    val messagePerMinute: Float

    enum class State {
        NoLogin, Loading, Online
    }

    val events: MutableList<BotEvent>

    suspend fun login(account: Long, password: String, configuration: BotConfiguration.() -> Unit = {})

    fun toBot(): Bot

    companion object {
        operator fun invoke(bot: Bot? = null): ComposeBot = ComposeBotImpl(bot)
    }
}

private class ComposeBotImpl(
    var _bot: Bot?,
) : ComposeBot {

    private var _avatar by mutableStateOf(ImageBitmap(200, 200))

    private var _messageCount by mutableStateOf(0L)

    private var _time by mutableStateOf(0L)

    override val time: Long get() = _time

    override var state: ComposeBot.State by mutableStateOf(_bot?.takeIf { it.isOnline }?.let { ComposeBot.State.Online }
        ?: ComposeBot.State.NoLogin)

    override val avatar: ImageBitmap get() = _avatar

    override suspend fun login(account: Long, password: String, configuration: BotConfiguration.() -> Unit) {
        kotlin.runCatching {
            state = ComposeBot.State.Loading
            _bot = MiraiConsole.addBot(account, password, configuration)
            subscribeEvent()
            _bot!!.login()
        }.onSuccess {
            state = ComposeBot.State.Online
            _bot!!.launch { loadResource() }
        }.onFailure {
            state = ComposeBot.State.NoLogin
        }
    }


    private var _messagePerMinute by mutableStateOf(0f)

    override val messagePerMinute: Float get() = _messagePerMinute

    override val events: MutableList<BotEvent> = mutableStateListOf()

    override fun toBot(): Bot = _bot ?: throw Exception("not bot")

    override val id: String
        get() = when (state) {
            ComposeBot.State.NoLogin -> "Unknown"
            ComposeBot.State.Loading -> "Loading"
            ComposeBot.State.Online -> _bot!!.id.toString()
        }

    override val nick: String
        get() = when (state) {
            ComposeBot.State.NoLogin -> "Unknown"
            ComposeBot.State.Loading -> "Loading"
            ComposeBot.State.Online -> _bot!!.nick
        }

    fun close(cause: Throwable?) = when (state) {
        ComposeBot.State.NoLogin -> Unit
        ComposeBot.State.Loading -> Unit
        ComposeBot.State.Online -> _bot!!.close(cause)
    }


    init {
        when (state) {
            ComposeBot.State.NoLogin -> Unit
            ComposeBot.State.Loading -> Unit
            ComposeBot.State.Online -> _bot!!.launch {
                loadResource()
                subscribeEvent()
            }
        }
    }

    private suspend fun loadResource() {
        _avatar = SkiaImageDecode(
            Mirai.Http.get(_bot!!.avatarUrl) {
                header("Connection", "close")
            }
        )
        _bot!!.eventChannel.subscribeAlways<MessageEvent> {
            _messageCount++
        }
        _bot!!.launch {
            while (true) {
                val currentMessage = _messageCount
                delay(10 * 1000)
                _messagePerMinute = (_messageCount - currentMessage) * 6f
                _messageCount = 0
            }
        }
    }

    private fun subscribeEvent() {
        _bot!!.eventChannel.subscribeAlways<BotEvent> {
            events.add(this)
        }
    }

}



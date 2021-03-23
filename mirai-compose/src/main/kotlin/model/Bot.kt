package com.youngerhousea.miraicompose.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.youngerhousea.miraicompose.utils.SkiaImageDecode
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.utils.BotConfiguration
import org.jetbrains.skija.Image

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

    suspend fun login(account: Long, password: String, configuration: BotConfiguration.() -> Unit = {})

    fun toBot(): Bot

    companion object {
        //only after longin
        operator fun invoke(bot: Bot? = null): ComposeBot = ComposeBotImpl(bot)

        val instances: MutableList<ComposeBot> = mutableStateListOf()
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
            _bot = MiraiConsole.addBot(account, password, configuration).alsoLogin()
        }.onSuccess {
            state = ComposeBot.State.Online
            _bot!!.launch { loadResource() }
        }.onFailure {
            state = ComposeBot.State.NoLogin
        }
    }


    private var _messagePerMinute by mutableStateOf(0f)

    override val messagePerMinute: Float get() = _messagePerMinute

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
            ComposeBot.State.Online -> _bot!!.launch { loadResource() }
        }
    }

    private suspend fun loadResource() {
        loadAvatar()
        startTiming()
    }

    private suspend fun loadAvatar() {
        _avatar = SkiaImageDecode(
            client.get(_bot!!.avatarUrl) {
                header("Connection", "close")
            }
        )
    }

    private suspend fun startTiming() {
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

}

private val client = HttpClient()


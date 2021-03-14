package com.youngerhousea.miraicompose.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import com.youngerhousea.miraicompose.utils.getAvatarImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.supervisorJob
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.MiraiLogger
import kotlin.coroutines.CoroutineContext


interface ComposeBot : Bot {

    val avatar: ImageBitmap

    val time: Long

    val messageCount: Long

    companion object {
        //only after longin
        operator fun invoke(bot: Bot): ComposeBot = ComposeBotImpl(bot)

        operator fun invoke(): ComposeBot = ComposeBotImpl()

        val instances: MutableList<ComposeBot> = mutableStateListOf()
    }
}


private class ComposeBotImpl(var _bot: Bot? = null) : Bot, ComposeBot {

    private var _avatar by mutableStateOf(ImageBitmap(200, 200))

    private var _messageCount by mutableStateOf(0L)

    private var _time by mutableStateOf(0L)

    override val avatar: ImageBitmap
        get() = _avatar

    override val messageCount: Long
        get() = _messageCount

    override val asFriend: Friend
        get() = _bot?.asFriend ?: throw Exception("No ok")

    override val asStranger: Stranger
        get() = _bot?.asStranger ?: throw Exception("No ok")

    override val configuration: BotConfiguration
        get() = _bot?.configuration ?: throw Exception("No ok")

    override val coroutineContext: CoroutineContext
        get() = _bot?.coroutineContext ?: throw Exception("No ok")

    override val eventChannel: EventChannel<BotEvent>
        get() = _bot?.eventChannel ?: throw Exception("No ok")

    override val friends: ContactList<Friend>
        get() = _bot?.friends ?: throw Exception("No ok")

    override val groups: ContactList<Group>
        get() = _bot?.groups ?: throw Exception("No ok")

    override val id: Long
        get() = _bot?.id ?: throw Exception("No ok")

    override val isOnline: Boolean
        get() = _bot?.isOnline ?: throw Exception("No ok")

    override val logger: MiraiLogger
        get() = _bot?.logger ?: throw Exception("No ok")

    override val nick: String
        get() = _bot?.nick ?: "Unknown"

    override val otherClients: ContactList<OtherClient>
        get() = _bot?.otherClients ?: throw Exception("No ok")

    override val strangers: ContactList<Stranger>
        get() = _bot?.strangers ?: throw Exception("No ok")

    override fun close(cause: Throwable?) {
        _bot?.close(cause)
    }

    override val time: Long get() = _time

    suspend fun login(account:Long, password:String) {
        _bot = BotFactory.newBot(account, password).alsoLogin()
        loadResource()
    }

    //Just for original bot!
    override suspend fun login() {
        _bot?.login()
        loadResource()
    }

    init {
        ComposeBot.instances.add(this)
        if (bot.isOnline) {
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


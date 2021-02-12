package com.youngerhousea.miraicompose.model

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import com.youngerhousea.miraicompose.utils.toAvatarImage
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.utils.BotConfiguration


class Model {
    val bots = mutableStateListOf(ComposeBot())

    var currentIndex by mutableStateOf(0)

    val currentBot get() = bots[currentIndex]

}


class ComposeBot {
    private lateinit var bot: Bot

    val logger get() = bot.logger

    var state: BotState by mutableStateOf(BotState.None)

    val messages = mutableStateListOf<MessageEvent>()
    val event = mutableStateListOf<BotEvent>()

    val nick
        get() = when (state) {
            BotState.None -> "未登录"
            BotState.Loading -> "加载中"
            BotState.Login -> bot.nick
        }

    val id
        get() = when (state) {
            BotState.None, BotState.Loading -> "..."
            BotState.Login -> bot.id.toString()
        }

    private var _avatar by mutableStateOf(ImageBitmap(200, 200))

    val avatar
        get() = when (state) {
            BotState.None, BotState.Loading -> _avatar
            BotState.Login -> {
                _avatar
            }
        }


    suspend fun login(account: String, password: String) {
        state = BotState.Loading
        bot = BotFactory.newBot(account.toLong(), password, BotConfiguration().apply {
            fileBasedDeviceInfo()
        }).alsoLogin()
        state = BotState.Login
        this@ComposeBot._avatar = bot.avatarUrl.toAvatarImage()
        bot.eventChannel.subscribeAlways<MessageEvent> {
            messages.add(this)
        }
        bot.eventChannel.subscribeAlways<BotEvent> {
            event.add(this)
        }
    }
}

class LoginWindowState {
    var invalidInputAccount by mutableStateOf(false)

    var exceptionPrompt by mutableStateOf("")

    var isException by mutableStateOf(false)

    var account by mutableStateOf("")

    var password by mutableStateOf("")
}


enum class BotState {
    None, Loading, Login
}
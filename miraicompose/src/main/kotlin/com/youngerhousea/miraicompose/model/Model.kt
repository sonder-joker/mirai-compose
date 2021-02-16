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

    val bots = mutableStateListOf<ComposeBot>()

    var currentIndex by mutableStateOf(-1)

    val currentBot get() = bots[currentIndex]

    init {
        Bot.instances.forEach {
            bots.add(ComposeBot(it))
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


class PluginState {
    var isSingleCard by mutableStateOf(false)
    var currentIndex by mutableStateOf(0)
}

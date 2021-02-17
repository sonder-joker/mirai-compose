package com.youngerhousea.miraicompose.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import net.mamoe.mirai.Bot


class Model {
    val bots = mutableStateListOf<ComposeBot>()

    var currentIndex by mutableStateOf(-1)

    val currentBot get() = bots[currentIndex]

    init {
        Bot.instances.forEach {
            this.bots.add(ComposeBot(it))
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

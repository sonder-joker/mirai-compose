package com.youngerhousea.miraicompose.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.input.TextFieldValue
import net.mamoe.mirai.Bot


class ModelImpl : Model {
    override val bots = mutableStateListOf<ComposeBot>()

    override var currentIndex by mutableStateOf(-1)

    init {
        Bot.instances.forEach {
            this.bots.add(ComposeBot(it))
        }
    }
}

interface Model {
    val bots: SnapshotStateList<ComposeBot>

    var currentIndex: Int

    val currentBot: ComposeBot get() = bots[currentIndex]

    companion object {
        operator fun invoke(): Model = ModelImpl()
    }
}

class LoginWindowState {
    var invalidInputAccount by mutableStateOf(false)

    var exceptionPrompt by mutableStateOf("")

    var isException by mutableStateOf(false)

    var account by mutableStateOf(TextFieldValue())

    var password by mutableStateOf(TextFieldValue())
}



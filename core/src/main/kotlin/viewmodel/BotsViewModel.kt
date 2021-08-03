package com.youngerhousea.miraicompose.core.viewmodel

import com.arkivanov.decompose.instancekeeper.InstanceKeeper
import com.youngerhousea.miraicompose.core.data.BotItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BotsViewModel(
) : InstanceKeeper.Instance {
    private val _botList = MutableStateFlow(listOf<BotItem>())

    private val _currentBot = MutableStateFlow<BotItem?>(null)

    val botList: StateFlow<List<BotItem>> get() = _botList

    val currentBot: StateFlow<BotItem?> get() = _currentBot

    fun setCurrentBot(bot: BotItem) {
        _currentBot.value = bot
    }

    fun addBot(bot: BotItem) {
        _botList.value += bot
    }

    fun addBot(bot: List<BotItem>) {
        _botList.value += bot
    }

    override fun onDestroy() {
//        botList.value.forEach(::cancel)

    }

}
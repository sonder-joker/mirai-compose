package com.youngerhousea.mirai.compose.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.youngerhousea.mirai.compose.console.MiraiCompose
import com.youngerhousea.mirai.compose.console.ViewModelScope
import com.youngerhousea.mirai.compose.console.doOnFinishAutoLogin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.mamoe.mirai.Bot

class HostViewModel : ViewModelScope() {
    private val _isExpand = mutableStateOf(false)

    private val _botList = mutableStateListOf<Bot>()

    private val _currentBot = mutableStateOf<Bot?>(null)

    val isExpand: State<Boolean> get() = _isExpand

    val botList: List<Bot> get() = _botList

    val currentBot: State<Bot?> get() = _currentBot

    fun openExpandMenu() {
        _isExpand.value = true
    }

     fun dismissExpandMenu() {
        _isExpand.value = false
    }

    init {
        MiraiCompose.lifecycle.doOnFinishAutoLogin {
            _botList.addAll(Bot.instances)
        }
    }
}


sealed class HostState {
    class ExpandMenu(
        val currentBot: Bot,
        val botList: List<Bot>
    ) : HostState()

    class Close(
        val currentBot: Bot
    )

}
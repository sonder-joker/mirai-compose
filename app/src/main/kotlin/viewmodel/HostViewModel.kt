package com.youngerhousea.mirai.compose.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.youngerhousea.mirai.compose.console.MiraiCompose
import com.youngerhousea.mirai.compose.console.ViewModelScope
import com.youngerhousea.mirai.compose.console.impl.doOnFinishAutoLogin
import net.mamoe.mirai.Bot

class HostViewModel : ViewModelScope() {
    private val _hostState = mutableStateOf(HostState())

    val hostState: State<HostState> get() = _hostState

    fun dispatch(event: Event) {
        _hostState.value = reduce(_hostState.value, event)
    }

    private fun reduce(state: HostState, event: Event): HostState {
        return when (event) {
            Event.OpenMenu -> state.copy(menuIsExpand = true)
            Event.CloseMenu -> state.copy(menuIsExpand = false)
            Event.OpenLoginDialog -> state.copy(loginDialogIsExpand = true)
            Event.CloseLoginDialog -> state.copy(loginDialogIsExpand = false)
            HostRoute.About -> state.copy(navigate = HostRoute.About)
            HostRoute.Message -> state.copy(navigate = HostRoute.Message)
            HostRoute.Plugins -> state.copy(navigate = HostRoute.Plugins)
            HostRoute.Setting -> state.copy(navigate = HostRoute.Setting)
            is HostRoute.BotMessage -> state.copy(navigate = HostRoute.BotMessage(event.bot))
        }
    }

    init {
        MiraiCompose.lifecycle.doOnFinishAutoLogin {
            _hostState.value = _hostState.value.copy(botList = Bot.instances)
        }
    }
}

sealed interface HostRoute {
    class BotMessage(val bot: Bot) : HostRoute, Event
    object Message : HostRoute, Event
    object Setting : HostRoute, Event
    object About : HostRoute, Event
    object Plugins : HostRoute, Event
}

data class HostState(
    val currentBot: Bot? = null,
    val botList: List<Bot> = listOf(),
    val menuIsExpand: Boolean = false,
    val loginDialogIsExpand: Boolean = false,
    val navigate: HostRoute = HostRoute.Message
)

sealed interface Event {
    object OpenLoginDialog : Event
    object CloseLoginDialog : Event
    object OpenMenu : Event
    object CloseMenu : Event
}
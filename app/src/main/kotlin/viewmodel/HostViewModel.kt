package com.youngerhousea.mirai.compose.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.youngerhousea.mirai.compose.console.ViewModelScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.BotOnlineEvent


interface Host {
    val hostState: State<HostState>

    fun dispatch(event: Action)

    sealed interface Route {
        class BotMessage(val bot: Bot) : Route, Action
        object Message : Route, Action
        object Setting : Route, Action
        object About : Route, Action
        object Plugins : Route, Action
        object ConsoleLog : Route, Action
    }

    @Immutable
    data class HostState(
        val currentBot: Bot? = null,
        val botList: List<Bot> = listOf(),
        val menuExpand: Boolean = false,
        val navigate: Route = Route.Message,
        val dialogExpand: Boolean = false
    )

    sealed interface Action {
        object OpenMenu : Action
        object CloseMenu : Action
        object OpenLoginDialog : Action
        object CloseLoginDialog : Action
    }
}


class HostViewModel : ViewModelScope(), Host {
    override val hostState = mutableStateOf(Host.HostState())

    override fun dispatch(event: Host.Action) {
        hostState.value = reduce(hostState.value, event)
    }

    private fun reduce(state: Host.HostState, action: Host.Action): Host.HostState {
        return when (action) {
            Host.Action.CloseLoginDialog -> state.copy(dialogExpand = false)
            Host.Action.OpenLoginDialog -> state.copy(dialogExpand = true)
            is Host.Action.OpenMenu -> state.copy(menuExpand = true)
            is Host.Action.CloseMenu -> state.copy(menuExpand = false)
            is Host.Route.About -> state.copy(navigate = action)
            is Host.Route.Message -> state.copy(navigate = action)
            is Host.Route.Plugins -> state.copy(navigate = action)
            is Host.Route.Setting -> state.copy(navigate = action)
            is Host.Route.BotMessage -> state.copy(currentBot = action.bot)
            is Host.Route.ConsoleLog -> state.copy(navigate = action)
        }
    }

    init {
        viewModelScope.launch {
            GlobalEventChannel.subscribeAlways<BotOnlineEvent> { event ->
                hostState.value =
                    hostState.value.copy(botList = hostState.value.botList + event.bot, currentBot = event.bot)
            }
        }
    }

}






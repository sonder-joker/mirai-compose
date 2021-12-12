package com.youngerhousea.mirai.compose.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.youngerhousea.mirai.compose.console.ViewModelScope
import com.youngerhousea.mirai.compose.console.impl.MiraiCompose
import com.youngerhousea.mirai.compose.console.impl.doOnFinishAutoLogin
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.BotOnlineEvent


interface Host {
    val hostState: State<HostState>

    fun dispatch(event: HostAction)
}


class HostViewModel : ViewModelScope(), Host {
    override val hostState = mutableStateOf(HostState())

    override fun dispatch(event: HostAction) {
        hostState.value = reduce(hostState.value, event)
    }

    private fun reduce(state: HostState, action: HostAction): HostState {
        return when (action) {
            is HostAction.OpenMenu -> state.copy(menuIsExpand = true)
            is HostAction.CloseMenu -> state.copy(menuIsExpand = false)
            is HostAction.OpenLoginDialog -> state.copy(loginDialogIsExpand = true, menuIsExpand = false)
            is HostAction.CloseLoginDialog -> state.copy(loginDialogIsExpand = false, menuIsExpand = false)
            is HostRoute.About -> state.copy(navigate = action)
            is HostRoute.Message -> state.copy(navigate = action)
            is HostRoute.Plugins -> state.copy(navigate = action)
            is HostRoute.Setting -> state.copy(navigate = action)
            is HostRoute.BotMessage -> state.copy(currentBot = action.bot)
            is HostRoute.ConsoleLog -> state.copy(navigate = action)
        }
    }

    init {
        MiraiCompose.lifecycle.doOnFinishAutoLogin {
            hostState.value = hostState.value.copy(botList = Bot.instances)
        }
        viewModelScope.launch {
            GlobalEventChannel.subscribeAlways<BotOnlineEvent> { event ->
                hostState.value = hostState.value.copy(botList = hostState.value.botList + event.bot, currentBot = event.bot)
            }
        }
    }

}

sealed interface HostRoute {
    class BotMessage(val bot: Bot) : HostRoute, HostAction
    object Message : HostRoute, HostAction
    object Setting : HostRoute, HostAction
    object About : HostRoute, HostAction
    object Plugins : HostRoute, HostAction
    object ConsoleLog : HostRoute, HostAction
}

@Immutable
data class HostState(
    val currentBot: Bot? = null,
    val botList: List<Bot> = listOf(),
    val menuIsExpand: Boolean = false,
    val loginDialogIsExpand: Boolean = false,
    val navigate: HostRoute = HostRoute.Message
)

sealed interface HostAction {
    object OpenLoginDialog : HostAction
    object CloseLoginDialog : HostAction
    object OpenMenu : HostAction
    object CloseMenu : HostAction
}




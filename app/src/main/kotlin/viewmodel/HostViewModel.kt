package com.youngerhousea.mirai.compose.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.youngerhousea.mirai.compose.console.MiraiCompose
import com.youngerhousea.mirai.compose.console.ViewModelScope
import com.youngerhousea.mirai.compose.console.impl.doOnFinishAutoLogin
import net.mamoe.mirai.Bot


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
            is HostAction.OpenLoginDialog -> state.copy(loginDialogIsExpand = true)
            is HostAction.CloseLoginDialog -> state.copy(loginDialogIsExpand = false)
            is HostRoute.About -> state.copy(navigate = action)
            is HostRoute.Message -> state.copy(navigate = action)
            is HostRoute.Plugins -> state.copy(navigate = action)
            is HostRoute.Setting -> state.copy(navigate = action)
            is HostRoute.BotMessage -> state.copy(navigate = action)
        }
    }

    init {
        MiraiCompose.lifecycle.doOnFinishAutoLogin {
            hostState.value = hostState.value.copy(botList = Bot.instances)
        }
    }

}

sealed interface HostRoute {
    class BotMessage(val bot: Bot) : HostRoute, HostAction
    object Message : HostRoute, HostAction
    object Setting : HostRoute, HostAction
    object About : HostRoute, HostAction
    object Plugins : HostRoute, HostAction
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
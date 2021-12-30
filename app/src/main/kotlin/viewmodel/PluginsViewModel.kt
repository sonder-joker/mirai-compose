package com.youngerhousea.mirai.compose.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.youngerhousea.mirai.compose.console.ViewModelScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.plugin.Plugin

interface Plugins {

    val state: State<InnerState>

    fun dispatch(action: Action)

    sealed interface Route {
        object List : Route, Action
        class Single(val plugin: Plugin) : Route, Action
    }

    sealed interface Action {
        class LoadingPlugin(val pluginName: String) : Action
        object OpenFileChooser : Action
    }


    data class InnerState(
        val navigate: Route = Route.List,
        val isFileChooserVisible: Boolean = false
    )
}

class PluginsViewModel : Plugins, ViewModelScope() {
    override val state = mutableStateOf(Plugins.InnerState())

    override fun dispatch(action: Plugins.Action) {
        viewModelScope.launch {
            state.value = reduce(action, state.value)
        }
    }

    private fun reduce(action: Plugins.Action, state: Plugins.InnerState): Plugins.InnerState {
        return when (action) {
            is Plugins.Route.List -> state.copy(navigate = action)
            is Plugins.Route.Single -> state.copy(navigate = action)
            is Plugins.Action.LoadingPlugin -> state
            is Plugins.Action.OpenFileChooser -> state.copy(isFileChooserVisible = true)
        }
    }
}

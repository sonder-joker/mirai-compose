package com.youngerhousea.mirai.compose.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.youngerhousea.mirai.compose.console.ViewModelScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.plugin.Plugin

class PluginsViewModel : ViewModelScope() {
    private val _state = mutableStateOf(PluginsState())

    val state: State<PluginsState> get() = _state

    fun dispatch(action: PluginAction) {
        viewModelScope.launch {
            _state.value = reduce(action, _state.value)
        }
    }

    private fun reduce(action: PluginAction, state: PluginsState): PluginsState {
        return when (action) {
            is PluginsRoute.List -> state.copy(navigate = action)
            is PluginsRoute.Single -> state.copy(navigate = action)
            is PluginAction.LoadingPlugin -> {
                state
            }
            is PluginAction.OpenFileChooser -> {
                state.copy(isFileChooserVisible = true)
            }
        }
    }
}

sealed interface PluginsRoute {
    object List : PluginsRoute, PluginAction
    class Single(val plugin: Plugin) : PluginsRoute, PluginAction
}

sealed interface PluginAction {
    class LoadingPlugin(val pluginName: String) : PluginAction
    object OpenFileChooser : PluginAction
}

data class PluginsState(
    val navigate: PluginsRoute = PluginsRoute.List,
    val isFileChooserVisible: Boolean = false
)

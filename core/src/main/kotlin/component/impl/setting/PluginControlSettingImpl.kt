package com.youngerhousea.miraicompose.core.component.impl.setting

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.core.component.setting.PluginControlSetting
import com.youngerhousea.miraicompose.core.pluginPath
import com.youngerhousea.miraicompose.core.utils.componentScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.nio.file.Path
import kotlin.io.path.moveTo

class PluginControlSettingImpl(
    componentContext: ComponentContext,
) : PluginControlSetting, ComponentContext by componentContext, CoroutineScope by componentContext.componentScope() {
    private val current = MutableStateFlow(pluginPath)

    override val error = MutableStateFlow(false)

    override fun addPlugin(file: Path) {
        try {
            file.moveTo(pluginPath)
        } catch (e: Throwable) {
            error.value = true

            launch {
                delay(3_000)
                error.value = false
            }
        }
    }

    override val currentPath: StateFlow<String> =
        current.mapState(this) { it.toString() }

}


fun <T, R> MutableStateFlow<T>.mapState(scope: CoroutineScope, transform: (T) -> R): StateFlow<R> =
    map { transform(it) }.stateIn(scope, SharingStarted.WhileSubscribed(), transform(value))

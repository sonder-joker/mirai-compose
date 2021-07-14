package com.youngerhousea.miraicompose.core.component.setting

import kotlinx.coroutines.flow.StateFlow
import java.nio.file.Path

interface PluginControlSetting {

    fun addPlugin(file: Path)

    val currentPath:StateFlow<String>

    val error: StateFlow<Boolean>
}
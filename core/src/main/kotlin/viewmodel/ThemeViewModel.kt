package com.youngerhousea.miraicompose.core.viewmodel

import com.arkivanov.decompose.instancekeeper.InstanceKeeper
import com.youngerhousea.miraicompose.core.data.LogColor
import com.youngerhousea.miraicompose.core.themeFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.decodeFromString
import net.mamoe.yamlkt.Yaml
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

class ThemeViewModel(
    val path: Path = themeFile
) : InstanceKeeper.Instance {
    val data: StateFlow<LogColor> get() = _data

    private val _data = MutableStateFlow(Yaml.decodeFromString(path.readText()) ?: LogColor())

    override fun onDestroy() {
        path.writeText(Yaml.encodeToString(_data.value))
    }

    fun setDebugColor(debug: String) {
        _data.value = _data.value.copy(debug = debug)
    }

    fun setVerboseColor(verbose: String) {
        _data.value = _data.value.copy(verbose = verbose)
    }

    fun setInfoColor(info: String) {
        _data.value = _data.value.copy(info = info)
    }

    fun setWarningColor(warning: String) {
        _data.value = _data.value.copy(warning = warning)
    }

    fun setErrorColor(error: String) {
        _data.value = _data.value.copy(error = error)
    }
}




package com.youngerhousea.miraicompose.core.component.setting

import com.youngerhousea.miraicompose.core.data.LogColor
import kotlinx.coroutines.flow.StateFlow

interface LogColorSetting {

    val logColor: StateFlow<LogColor>

    fun setDebugColor(debug: String)

    fun setVerboseColor(verbose: String)

    fun setInfoColor(info: String)

    fun setErrorColor(error: String)

    fun setWarningColor(warning: String)

    fun onExitButtonClicked()
}
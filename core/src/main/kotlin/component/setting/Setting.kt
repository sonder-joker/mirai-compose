package com.youngerhousea.miraicompose.core.component.setting

import com.youngerhousea.miraicompose.core.console.LogPriority
import com.youngerhousea.miraicompose.core.data.LogColor
import com.youngerhousea.miraicompose.core.data.LoginCredential
import com.youngerhousea.miraicompose.core.viewmodel.Node
import kotlinx.coroutines.flow.StateFlow


/**
 * Compose各项参数的设置
 *
 * TODO:提供注释
 */

interface Setting {
//    val data: List<PluginData>
//    val config: List<PluginConfig>

    val logLevelSetting: LogLevelSetting

    val logColorSetting: LogColorSetting

    val autoLoginSetting: AutoLoginSetting
}


interface LogColorSetting {

    val logColor:StateFlow<LogColor>

    fun setDebugColor(debug: String)

    fun setVerboseColor(verbose: String)

    fun setInfoColor(info: String)

    fun setErrorColor(error: String)

    fun setWarningColor(warning: String)
}

interface LogLevelSetting {

    val node: StateFlow<Node>

    fun setLogConfigLevel(priority: LogPriority)
}


interface AutoLoginSetting {

    val model: StateFlow<List<LoginCredential>>

    fun addAutoLogin(config: LoginCredential)

    fun updateLoginCredential(index: Int, loginCredential: LoginCredential)

    fun addLoginCredential(loginCredential: LoginCredential)
}




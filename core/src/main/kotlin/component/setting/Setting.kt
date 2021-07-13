package com.youngerhousea.miraicompose.core.component.setting

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value


/**
 * Compose各项参数的设置
 *
 * TODO:提供注释
 */

interface Setting {
//    val data: List<PluginData>
//    val config: List<PluginConfig>

    val state: Value<RouterState<*, Child>>


    sealed class Child {
        class LogLevel(val logLevelSetting: LogLevelSetting) : Child()
        class LogColor(val logColorSetting: LogColorSetting) : Child()
        class AutoLogin(val autoLoginSetting: AutoLoginSetting) : Child()
        class Main(val main: MainSetting) : Child()
    }
}




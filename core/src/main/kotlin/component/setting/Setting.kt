package com.youngerhousea.miraicompose.core.component.setting

import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.internal.data.builtins.AutoLoginConfig
import net.mamoe.mirai.console.logging.AbstractLoggerController
import net.mamoe.yamlkt.YamlDynamicSerializer


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
    val debug: StringColor

    val verbose: StringColor

    val info: StringColor

    val warning: StringColor

    val error: StringColor

    fun onDebugColorSet(stringColor: StringColor)

    fun onVerboseColorSet(stringColor: StringColor)

    fun onInfoColorSet(stringColor: StringColor)

    fun onErrorColorSet(stringColor: StringColor)

    fun onWarningColorSet(stringColor: StringColor)
}

interface LogLevelSetting {
    val logConfigLevel: AbstractLoggerController.LogPriority

    fun setLogConfigLevel(priority: AbstractLoggerController.LogPriority)
}


interface AutoLoginSetting {
    data class Model(
        val accountList: List<AutoLoginConfig.Account>
    )

    val model: StateFlow<Model>

    fun addAutoLogin(account: String, password: AutoLoginConfig.Account.Password, configuration:Map<AutoLoginConfig.Account.ConfigurationKey, @Serializable(with = YamlDynamicSerializer::class) Any>)
}

typealias StringColor = String
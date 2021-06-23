package com.youngerhousea.miraicompose.core.component.setting

import net.mamoe.mirai.console.logging.AbstractLoggerController


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

typealias StringColor = String
package com.youngerhousea.miraicompose.core.component.setting


/**
 * Compose各项参数的设置
 *
 * TODO:提供注释
 */

interface Setting {
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

typealias StringColor = String
package com.youngerhousea.miraicompose.component.setting

import androidx.compose.ui.graphics.Color

/**
 * Compose各项参数的设置
 *
 * TODO:提供注释
 */

interface Setting {
    val debug: Color

    val verbose: Color

    val info: Color

    val warning: Color

    val error: Color

    fun onDebugColorSet(color: Color)

    fun onVerboseColorSet(color: Color)

    fun onInfoColorSet(color: Color)

    fun onErrorColorSet(color: Color)

    fun onWarningColorSet(color: Color)
}
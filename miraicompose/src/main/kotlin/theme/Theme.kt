package com.youngerhousea.miraicompose.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
data class InternalColor(
    val backgroundDark: Long = 0xFF2B2B2B,
    val backgroundMedium: Long = 0xFF3C3F41,
    val backgroundLight: Long = 0xFF4E5254,
    val backgroundDarkGray: Long = 0xFF444444,
) {

    val co = Color.Black
    @Transient
    val material: Colors = darkColors(
        background = Color(backgroundDark),
        surface = Color(backgroundMedium),
        primary = Color.White
    )
}

@Serializable
data class LogColor(
    var verbose: Long = 0xFF00FF00,
    var info: Long = 0xFF00FF00,
    var warning: Long = 0xFFFFFF00,
    var error: Long = 0xFFFFFF00,
    var debug: Long = 0xFFCCCCCC,
)

object AppTheme {
    val logColors = LogColor()
    val themColors = InternalColor()
}
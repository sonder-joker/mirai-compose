package com.youngerhousea.miraicompose.theme

import androidx.compose.material.darkColors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable


@Serializable
data class InternalColor(
    val backgroundDark: Long = 0xFF2B2B2B,
    val backgroundMedium: Long = 0xFF3C3F41,
    val backgroundLight: Long = 0xFF4E5254,
    val backgroundDarkGray: Long = 0xFF444444,
    var verbose: Long = 0xFF00FF00,
    var info: Long = 0xFF00FF00,
    var warning: Long = 0xFFFFFF00,
    var error: Long = 0xFFFFFF00,
    var debug: Long = 0xFFCCCCCC
)

object AppTheme {

    fun to() {

    }

    object Colors {
        val backgroundDark: Color by mutableStateOf(Color(0xFF2B2B2B))
        val backgroundMedium: Color by mutableStateOf(Color(0xFF3C3F41))
        val backgroundLight: Color by mutableStateOf(Color(0xFF4E5254))
        val backgroundDarkGray: Color by mutableStateOf(Color(0xFF444444))

        val material: androidx.compose.material.Colors by mutableStateOf(
            darkColors(
                background = backgroundDark,
                surface = backgroundMedium,
                primary = Color.White
            )
        )
    }

    object LogColor {
        var verbose: Color by mutableStateOf(Color.Green)
        var info: Color by mutableStateOf(Color.Green)
        var warning: Color by mutableStateOf(Color.Yellow)
        var error: Color by mutableStateOf(Color.Red)
        var debug: Color by mutableStateOf(Color.LightGray)
    }
}
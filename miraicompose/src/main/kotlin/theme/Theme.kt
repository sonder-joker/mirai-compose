package com.youngerhousea.miraicompose.theme

import androidx.compose.material.darkColors
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
object AppTheme {

    @Serializable
    object Colors {
        val backgroundDark: Color by mutableStateOf(Color(0xFF2B2B2B))
        val backgroundMedium: Color by mutableStateOf(Color(0xFF3C3F41))
        val backgroundLight: Color by mutableStateOf(Color(0xFF4E5254))
        val backgroundDarkGray: Color by mutableStateOf(Color.DarkGray)

        val material: androidx.compose.material.Colors by mutableStateOf(
            darkColors(
                background = backgroundDark,
                surface = backgroundMedium,
                primary = Color.White
            )
        )
    }

    @Serializable
    object LogColor {
        var verbose: Color by mutableStateOf(Color.Green)
        var info: Color by mutableStateOf(Color.Green)
        var warning: Color by mutableStateOf(Color.Yellow)
        var error: Color by mutableStateOf(Color.Red)
        var debug: Color by mutableStateOf(Color.LightGray)
    }
}
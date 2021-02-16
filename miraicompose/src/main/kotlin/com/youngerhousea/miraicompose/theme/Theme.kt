package com.youngerhousea.miraicompose.theme

import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color

object AppTheme {

    object Colors {
        val backgroundDark: Color = Color(0xFF2B2B2B)
        val backgroundMedium: Color = Color(0xFF3C3F41)
        val backgroundLight: Color = Color(0xFF4E5254)
        val backgroundDarkGray: Color = Color.DarkGray

        val material: androidx.compose.material.Colors = darkColors(
            background = backgroundDark,
            surface = backgroundMedium,
            primary = Color.White
        )
    }

    object LogColor{
        var verbose: Color = Color.Green
        var info: Color = Color.Green
        var warning: Color = Color.Yellow
        var error: Color = Color.Red
        var debug: Color = Color.LightGray
    }
}
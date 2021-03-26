package com.youngerhousea.miraicompose

import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import com.youngerhousea.miraicompose.theme.AppTheme
import com.youngerhousea.miraicompose.theme.ColorSerializer
import com.youngerhousea.miraicompose.theme.ColorsSerializer
import com.youngerhousea.miraicompose.theme.ComposeSetting
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.mamoe.yamlkt.Yaml
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

internal class ColorTest {
    private val yaml = Yaml { }
    @Test
    fun colorTest() {
        val testColor = Color(0x600000)
        val data = yaml.encodeToString(ColorSerializer, testColor)
        val color = yaml.decodeFromString(ColorSerializer, data)
        assertEquals(color, testColor)
        assertEquals(data, """
            '#600000'
        """.trimIndent())
    }

    @Test
    fun colorsTest() {
        val lightColors = lightColors(
            primary = Color(0xFF6200EE),
            primaryVariant = Color(0xFF3700B3),
            secondary = Color(0xFF03DAC6)
        )
        val lightColorsString = yaml.encodeToString(ColorsSerializer, lightColors)
        val decodeLightColors = yaml.decodeFromString(ColorsSerializer, lightColorsString)

        assertEquals(
            yaml.encodeToString(ColorSerializer, lightColors.background),
            yaml.encodeToString(ColorSerializer, decodeLightColors.background)
        )
        assertEquals(
            yaml.encodeToString(ColorSerializer, lightColors.onBackground),
            yaml.encodeToString(ColorSerializer, decodeLightColors.onBackground)
        )
        assertEquals(
            yaml.encodeToString(ColorSerializer, lightColors.primary),
            yaml.encodeToString(ColorSerializer, decodeLightColors.primary)
        )
        assertEquals(
            yaml.encodeToString(ColorSerializer, lightColors.onPrimary),
            yaml.encodeToString(ColorSerializer, decodeLightColors.onPrimary)
        )

    }

//    Theme can't be same because of float?
//    @Test
//    fun appThemeTest() {
//        val theme = yaml.encodeToString(ComposeSetting.AppTheme)
//
//        val data = yaml.decodeFromString<AppTheme>(theme)
//        assertEquals(ComposeSetting.AppTheme, data)
//    }
}


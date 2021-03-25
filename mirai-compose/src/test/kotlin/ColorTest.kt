package com.youngerhousea.miraicompose

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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

internal class ColorTest {
    private val yaml = Json {}
//    private val yaml = Yaml { }

    @Test
    fun getColor() {
        val testColor = Color(0x600000)
        val data = yaml.encodeToString(ColorSerializer, testColor)
        val color = yaml.decodeFromString(ColorSerializer, data)
        assertEquals(testColor, color)
    }

    @Test
    fun colorsTest() {
        val theme = yaml.encodeToString(ColorsSerializer, ComposeSetting.AppTheme.materialLight)
        val data = yaml.decodeFromString(ColorsSerializer, theme)
        assertEquals(yaml.encodeToString(ColorSerializer,ComposeSetting.AppTheme.materialLight.background), yaml.encodeToString(ColorSerializer,data.background))
        assertEquals(yaml.encodeToString(ColorSerializer,ComposeSetting.AppTheme.materialLight.onBackground), yaml.encodeToString(ColorSerializer,data.onBackground))
        assertEquals(yaml.encodeToString(ColorSerializer,ComposeSetting.AppTheme.materialLight.primary), yaml.encodeToString(ColorSerializer,data.primary))
        assertEquals(yaml.encodeToString(ColorSerializer,ComposeSetting.AppTheme.materialLight.onPrimary), yaml.encodeToString(ColorSerializer,data.onPrimary))

    }

    @Test
    fun appThemeTest() {
        val theme = yaml.encodeToString(ComposeSetting.AppTheme)

        val data = yaml.decodeFromString<AppTheme>(theme)
        assertEquals(ComposeSetting.AppTheme, data)
    }


}


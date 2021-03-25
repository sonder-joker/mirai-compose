package com.youngerhousea.miraicompose

import androidx.compose.ui.graphics.Color
import com.youngerhousea.miraicompose.theme.AppTheme
import com.youngerhousea.miraicompose.theme.ColorSerializer
import com.youngerhousea.miraicompose.theme.ColorsSerializer
import com.youngerhousea.miraicompose.theme.ComposeSetting
import kotlinx.serialization.decodeFromString
import net.mamoe.yamlkt.Yaml
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ColorTest {

    private val yaml = Yaml { }
    private val testColor = Color(232, 224, 203)

    @Test
    fun getColor() {
        println(yaml.encodeToString(ColorSerializer, testColor))
    }

    @Test
    fun colorsTest() {
        val theme = yaml.encodeToString(ColorsSerializer, ComposeSetting.AppTheme.materialLight)
        val data = yaml.decodeFromString(ColorsSerializer, theme)
        assertEquals(ComposeSetting.AppTheme.materialLight.background, data.background)
        assertEquals(ComposeSetting.AppTheme.materialLight.error, data.background)

    }

    @Test
    fun appThemeTest() {
        val theme = yaml.encodeToString(ComposeSetting.AppTheme)
        println(theme)

        val data = yaml.decodeFromString<AppTheme>(theme)
        assertEquals(ComposeSetting.AppTheme, data)
    }


}


//package com.youngerhousea.miraicompose.theme
//
//import androidx.compose.material.Colors
//import androidx.compose.material.lightColors
//import androidx.compose.ui.graphics.Color
//import kotlinx.serialization.decodeFromString
//import kotlinx.serialization.encodeToString
//import net.mamoe.yamlkt.Yaml
//import org.junit.jupiter.api.Test
//import kotlin.test.assertEquals
//
//internal class ColorTest {
//
//    @Test
//    fun colorTest() {
//
//    }
//
//    @Test
//    fun colorsTest() {
//        val lightColors = lightColors(
//            primary = Color(0xFF6200EE),
//            primaryVariant = Color(0xFF3700B3),
//            secondary = Color(0xFF03DAC6)
//        )
//        val lightColorsString = Yaml.Default.encodeToString(lightColors)
//        val decodeLightColors = Yaml.Default.decodeFromString<Colors>(lightColorsString)
//
//        assertEquals(
//            Yaml.Default.encodeToString<Color>(lightColors.background),
//            Yaml.Default.encodeToString<Color>(decodeLightColors.background)
//        )
//        assertEquals(
//            Yaml.Default.encodeToString<Color>(lightColors.onBackground),
//            Yaml.Default.encodeToString<Color>(decodeLightColors.onBackground)
//        )
//        assertEquals(
//            Yaml.Default.encodeToString<Color>(lightColors.primary),
//            Yaml.Default.encodeToString<Color>(decodeLightColors.primary)
//        )
//        assertEquals(
//            Yaml.Default.encodeToString<Color>(lightColors.onPrimary),
//            Yaml.Default.encodeToString<Color>(decodeLightColors.onPrimary)
//        )
//
//    }
//
////    Theme can't be same because of float?
////    @Test
////    fun appThemeTest() {
////        val theme = yaml.encodeToString(ComposeSetting.AppTheme)
////
////        val data = yaml.decodeFromString<AppTheme>(theme)
////        assertEquals(ComposeSetting.AppTheme, data)
////    }
//}
//

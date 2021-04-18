import androidx.compose.ui.graphics.Color
import com.youngerhousea.miraicompose.utils.r
import okhttp3.internal.toHexString

//package com.youngerhousea.miraicompose
//
//import androidx.compose.material.Colors
//import androidx.compose.material.lightColors
//import androidx.compose.ui.graphics.Color
//import kotlinx.serialization.ExperimentalSerializationApi
//import kotlinx.serialization.decodeFromString
//import kotlinx.serialization.encodeToString
//import net.mamoe.yamlkt.Yaml
//import org.junit.jupiter.api.Test
//import kotlin.test.assertEquals
//
//internal class ColorTest {
//    @OptIn(ExperimentalSerializationApi::class)
//    private val yaml = Yaml(themeModule) {}
//
//    @Test
//    fun colorTest() {
//        val testColor = Color(0x600000)
//        val data = yaml.encodeToString(testColor)
//        val color = yaml.decodeFromString<Color>(data)
//        assertEquals(color, testColor)
//        assertEquals(
//            data, """
//            '#600000'
//        """.trimIndent()
//        )
//    }
//
//    @Test
//    fun colorsTest() {
//        val lightColors = lightColors(
//            primary = Color(0xFF6200EE),
//            primaryVariant = Color(0xFF3700B3),
//            secondary = Color(0xFF03DAC6)
//        )
//        val lightColorsString = yaml.encodeToString(lightColors)
//        val decodeLightColors = yaml.decodeFromString<Colors>(lightColorsString)
//
//        assertEquals(
//            yaml.encodeToString<Color>(lightColors.background),
//            yaml.encodeToString<Color>(decodeLightColors.background)
//        )
//        assertEquals(
//            yaml.encodeToString<Color>(lightColors.onBackground),
//            yaml.encodeToString<Color>(decodeLightColors.onBackground)
//        )
//        assertEquals(
//            yaml.encodeToString<Color>(lightColors.primary),
//            yaml.encodeToString<Color>(decodeLightColors.primary)
//        )
//        assertEquals(
//            yaml.encodeToString<Color>(lightColors.onPrimary),
//            yaml.encodeToString<Color>(decodeLightColors.onPrimary)
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

fun main() {
    val c = Color(0x3cfdfafa)
    println(c.r.toHexString())
}

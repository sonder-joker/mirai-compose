package com.youngerhousea.miraicompose.app.utils

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.builtins.serializer
import net.mamoe.yamlkt.Yaml
import org.junit.Test
import kotlin.test.assertEquals

class CustomSerializerTest {
    private fun serialize(color: Color): Color {
        val stringColor = Yaml.encodeToString(ColorSerializer, color)
        return Yaml.decodeFromString(ColorSerializer, stringColor)
    }

    @Test
    fun rgba() {
        val color = Color(1, 1, 1, 1)
        assertEquals(color, serialize(color), "RGBA Color Serializer")
    }

    @Test
    fun long() {
        val color = Color(0xffffffff)
        assertEquals(color, serialize(color), "Long Color Serializer")
    }

    @Test
    fun colors() {
//        val lightColors = lightColors()
//        val decodeLightColors =
//            Yaml.decodeFromString(ColorsSerializer, Yaml.encodeToString(ColorsSerializer, lightColors))
//
//        assertEquals(
//            lightColors.background,
//            decodeLightColors.background
//        )
//        assertEquals(
//            lightColors.onBackground,
//            decodeLightColors.onBackground
//        )
//        assertEquals(
//            lightColors.primary,
//            decodeLightColors.primary
//        )
//        assertEquals(
//            Yaml.encodeToString(ColorSerializer, lightColors.onPrimary),
//            Yaml.encodeToString(ColorSerializer, decodeLightColors.onPrimary)
//        )
    }

    @Test
    fun mutableState() {
        val stateInt = mutableStateOf(0)
        val newStateInt =
            Yaml.decodeFromString(
                MutableStateSerializer(Int.serializer()),
                Yaml.encodeToString(MutableStateSerializer(Int.serializer()), stateInt)
            )
        assertEquals(
            stateInt.value,
            newStateInt.value
        )
    }

    @Test
    fun appTheme() {
//        assertEquals(
//            ComposeSetting.AppTheme.logColors,
//            Yaml.decodeFromString(
//                AppTheme.serializer(),
//                Yaml.encodeToString(ComposeSetting.AppTheme)
//            ).logColors
//        )
    }
}
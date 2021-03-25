@file:UseSerializers(ColorSerializer::class)

package com.youngerhousea.miraicompose.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

fun Int.toHexString(): String = Integer.toHexString(this)

internal object ColorSerializer : KSerializer<Color> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Color", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeString("#${value.toArgb().toHexString()}")
    }

    override fun deserialize(decoder: Decoder): Color =
        Color(decoder.decodeString().removePrefix("#").toLong(16))
}


@Serializable
internal data class InternalColor(
    //dark
    val background: Color = Color(0xFF2B2B2B),
    //medium
    val surface: Color = Color(0xFF3C3F41),
    //light
    val secondary: Color = Color(0xFF4E5254),
    //dark gray
    val primary: Color = Color(0xFF444444),
) {
    @Transient
    val materialDark: Colors = darkColors(
//        background = background,
//        surface = surface,
//        primary = primary,
//        secondary = secondary
    )

    @Transient
    var materialLight: Colors by mutableStateOf( lightColors(
//        background = Color(0xf5FFFFFF),
        surface = Color(0xff979595),
        primary = Color(0xf5f5f5f5),
        onPrimary = Color.Black
//        secondary = backgroundLight
    ))
}

@Serializable
internal data class LogColor(
    var verbose: Color = Color(0xFF00FF00),
    var info: Color = Color(0xFF00FF00),
    var warning: Color = Color(0xFFFFFF00),
    var error: Color = Color(0xFFFFFF00),
    var debug: Color = Color(0xFFCCCCCC),
)

@Serializable
internal data class AppTheme(
    val logColors: LogColor = LogColor(),
    val themeColors: InternalColor = InternalColor()
)

internal object ComposeSetting : AutoSavePluginConfig("ComposeSetting") {
    val AppTheme by value(AppTheme())
}


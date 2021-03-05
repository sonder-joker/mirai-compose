@file:UseSerializers(ColorSerializer::class)

package com.youngerhousea.miraicompose.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.colorspace.connect
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


object ColorSerializer : KSerializer<Color> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Color", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeString(value.value.toString())
    }

    override fun deserialize(decoder: Decoder): Color =
        Color(decoder.decodeString().toULong())
}

@Serializable
data class InternalColor(
    val backgroundDark: Color = Color(0xFF2B2B2B),
    val backgroundMedium: Color = Color(0xFF3C3F41),
    val backgroundLight: Color = Color(0xFF4E5254),
    val backgroundDarkGray: Color = Color(0xFF444444),
) {
    @Transient
    val materialDark: Colors = darkColors(
        background = backgroundDark,
        surface = backgroundMedium,
        primary = backgroundDarkGray,
        secondary = backgroundLight
    )


    @Transient
    val materialLight: Colors = lightColors(
//        background = backgroundDark,
//        surface = backgroundMedium,
//        primary = backgroundDarkGray,
//        secondary = backgroundLight
    )
}


@Serializable
data class LogColor(
    var verbose: Color = Color(0xFF00FF00),
    var info: Color = Color(0xFF00FF00),
    var warning: Color = Color(0xFFFFFF00),
    var error: Color = Color(0xFFFFFF00),
    var debug: Color = Color(0xFFCCCCCC),
)

@Serializable
data class InternalAppTheme(
    val logColors: LogColor = LogColor(),
    val themeColors: InternalColor = InternalColor()
)

val AppTheme = InternalAppTheme()

package com.youngerhousea.miraicompose.app.utils

import androidx.compose.material.Colors
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.*

private fun Int.toHexString(): String = Integer.toHexString(this).padStart(8, '0')

@OptIn(ExperimentalSerializationApi::class)
internal object ColorSerializer : KSerializer<Color> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Color", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Color) =
        encoder.encodeString(value.toArgb().toHexString())

    override fun deserialize(decoder: Decoder): Color =
        Color(decoder.decodeString().toLong(16))
}

@OptIn(ExperimentalSerializationApi::class)
class MutableStateSerializer<T : Any>(private val dataSerializer: KSerializer<T>) : KSerializer<MutableState<T>> {
    override val descriptor: SerialDescriptor = dataSerializer.descriptor

    override fun deserialize(decoder: Decoder): MutableState<T> = mutableStateOf(dataSerializer.deserialize(decoder))

    override fun serialize(encoder: Encoder, value: MutableState<T>) = dataSerializer.serialize(encoder, value.value)
}

@OptIn(ExperimentalSerializationApi::class)
internal object ColorsSerializer : KSerializer<Colors> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Colors") {
            element("primary", ColorSerializer.descriptor)
            element("primaryVariant", ColorSerializer.descriptor)
            element("secondary", ColorSerializer.descriptor)
            element("secondaryVariant", ColorSerializer.descriptor)
            element("background", ColorSerializer.descriptor)
            element("surface", ColorSerializer.descriptor)
            element("error", ColorSerializer.descriptor)
            element("onPrimary", ColorSerializer.descriptor)
            element("onSecondary", ColorSerializer.descriptor)
            element("onBackground", ColorSerializer.descriptor)
            element("onSurface", ColorSerializer.descriptor)
            element("onError", ColorSerializer.descriptor)
            element("isLight", ColorSerializer.descriptor)
        }

    override fun serialize(encoder: Encoder, value: Colors) =
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(descriptor, 0, ColorSerializer, value.primary)
            encodeSerializableElement(descriptor, 1, ColorSerializer, value.primaryVariant)
            encodeSerializableElement(descriptor, 2, ColorSerializer, value.secondary)
            encodeSerializableElement(descriptor, 3, ColorSerializer, value.secondaryVariant)
            encodeSerializableElement(descriptor, 4, ColorSerializer, value.background)
            encodeSerializableElement(descriptor, 5, ColorSerializer, value.surface)
            encodeSerializableElement(descriptor, 6, ColorSerializer, value.error)
            encodeSerializableElement(descriptor, 7, ColorSerializer, value.onPrimary)
            encodeSerializableElement(descriptor, 8, ColorSerializer, value.onSecondary)
            encodeSerializableElement(descriptor, 9, ColorSerializer, value.onBackground)
            encodeSerializableElement(descriptor, 10, ColorSerializer, value.onSurface)
            encodeSerializableElement(descriptor, 11, ColorSerializer, value.onError)
            encodeBooleanElement(descriptor, 12, value.isLight)
        }


    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): Colors =
        decoder.decodeStructure(descriptor) {
            var a0 = Color(0)
            var a1 = Color(0)
            var a2 = Color(0)
            var a3 = Color(0)
            var a4 = Color(0)
            var a5 = Color(0)
            var a6 = Color(0)
            var a7 = Color(0)
            var a8 = Color(0)
            var a9 = Color(0)
            var a10 = Color(0)
            var a11 = Color(0)
            var a12 = false
            if (decodeSequentially()) {
                a0 = decodeSerializableElement(descriptor, 0, ColorSerializer)
                a1 = decodeSerializableElement(descriptor, 1, ColorSerializer)
                a2 = decodeSerializableElement(descriptor, 2, ColorSerializer)
                a3 = decodeSerializableElement(descriptor, 3, ColorSerializer)
                a4 = decodeSerializableElement(descriptor, 4, ColorSerializer)
                a5 = decodeSerializableElement(descriptor, 5, ColorSerializer)
                a6 = decodeSerializableElement(descriptor, 6, ColorSerializer)
                a7 = decodeSerializableElement(descriptor, 7, ColorSerializer)
                a8 = decodeSerializableElement(descriptor, 8, ColorSerializer)
                a9 = decodeSerializableElement(descriptor, 9, ColorSerializer)
                a10 = decodeSerializableElement(descriptor, 10, ColorSerializer)
                a11 = decodeSerializableElement(descriptor, 11, ColorSerializer)
                a12 = decodeBooleanElement(descriptor, 12)
            } else while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> a0 = decodeSerializableElement(descriptor, 0, ColorSerializer)
                    1 -> a1 = decodeSerializableElement(descriptor, 1, ColorSerializer)
                    2 -> a2 = decodeSerializableElement(descriptor, 2, ColorSerializer)
                    3 -> a3 = decodeSerializableElement(descriptor, 3, ColorSerializer)
                    4 -> a4 = decodeSerializableElement(descriptor, 4, ColorSerializer)
                    5 -> a5 = decodeSerializableElement(descriptor, 6, ColorSerializer)
                    6 -> a6 = decodeSerializableElement(descriptor, 6, ColorSerializer)
                    7 -> a7 = decodeSerializableElement(descriptor, 7, ColorSerializer)
                    8 -> a8 = decodeSerializableElement(descriptor, 8, ColorSerializer)
                    9 -> a9 = decodeSerializableElement(descriptor, 9, ColorSerializer)
                    10 -> a10 = decodeSerializableElement(descriptor, 10, ColorSerializer)
                    11 -> a11 = decodeSerializableElement(descriptor, 11, ColorSerializer)
                    12 -> a12 = decodeBooleanElement(descriptor, 12)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Colors(
                a0,
                a1,
                a2,
                a3,
                a4,
                a5,
                a6,
                a7,
                a8,
                a9,
                a10,
                a11,
                a12
            )
        }
}

val Color.r get() = toArgb() and 0xff000000.toInt() ushr 24

val Color.g get() = toArgb() and 0x00ff0000 ushr 16

val Color.b get() = toArgb() and 0x0000ff00 ushr 8

val Color.a get() = toArgb() and 0x000000ff
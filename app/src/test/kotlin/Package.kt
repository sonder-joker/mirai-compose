package com.youngerhousea.miraicompose.app

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encodeToString
import net.mamoe.yamlkt.Yaml
import okhttp3.internal.toHexString

@Serializable
data class LogColor(
    val debug: ULong = 4278255615u,
    val verbose: ULong = 4294902015u,
    val info: ULong = 4278295886u,
    val warning: ULong = 4294091025u,
    val error: ULong = 4293540955u
)

fun main() {
    val blue = Color(0xFF0000FF)
    println("0xFF00FFFF".toLong(16))
}


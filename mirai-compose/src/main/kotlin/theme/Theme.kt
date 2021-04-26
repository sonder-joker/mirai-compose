package com.youngerhousea.miraicompose.theme

import androidx.compose.material.Colors
import androidx.compose.material.lightColors
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.youngerhousea.miraicompose.utils.ColorSerializer
import com.youngerhousea.miraicompose.utils.ColorsSerializer
import com.youngerhousea.miraicompose.utils.MutableStateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value
import kotlin.math.log


@Serializable
class LogColor {
    @SerialName("debug")
    @Serializable(MutableStateSerializer::class)
    private var _debug: MutableState<@Serializable(ColorSerializer::class) Color> = mutableStateOf(Color.Cyan)

    @SerialName("verbose")
    @Serializable(MutableStateSerializer::class)
    private var _verbose: MutableState<@Serializable(ColorSerializer::class) Color> = mutableStateOf(Color.Magenta)

    @SerialName("info")
    @Serializable(MutableStateSerializer::class)
    private var _info: MutableState<@Serializable(ColorSerializer::class) Color> = mutableStateOf(Color.Green)

    @SerialName("warning")
    @Serializable(MutableStateSerializer::class)
    private var _warning: MutableState<@Serializable(ColorSerializer::class) Color> = mutableStateOf(Color.Yellow)

    @SerialName("error")
    @Serializable(MutableStateSerializer::class)
    private var _error: MutableState<@Serializable(ColorSerializer::class) Color> = mutableStateOf(Color.Red)

    var debug: Color by _debug

    var verbose: Color by _verbose

    var info: Color by _info

    var warning: Color by _warning

    var error: Color by _error

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LogColor

        if (debug != other.debug) return false
        if (verbose != other.verbose) return false
        if (info != other.info) return false
        if (warning != other.warning) return false
        if (error != other.error) return false

        return true
    }

    override fun hashCode(): Int {
        var result = debug.hashCode()
        result = 31 * result + verbose.hashCode()
        result = 31 * result + info.hashCode()
        result = 31 * result + warning.hashCode()
        result = 31 * result + error.hashCode()
        return result
    }

}

@Serializable
class AppTheme {
    @SerialName("materialLight")
    @Serializable(MutableStateSerializer::class)
    private val _materialLight: MutableState<@Serializable(ColorsSerializer::class) Colors> = mutableStateOf(
        lightColors(
            surface = Color(0xff979595),
            onSurface = Color(0xFFFFFFFF),
            primary = Color(0xf5f5f5f5),
            onPrimary = Color(0xFF000000)
        )
    )

    val logColors: LogColor = LogColor()

    var materialLight: Colors by _materialLight
}

internal object ComposeSetting : AutoSavePluginConfig("ComposeSetting") {
    val AppTheme: AppTheme by value(AppTheme())
}





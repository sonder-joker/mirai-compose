package com.youngerhousea.miraicompose.theme

import androidx.compose.material.Colors
import androidx.compose.material.lightColors
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.youngerhousea.miraicompose.console.ComposeDataScope
import com.youngerhousea.miraicompose.utils.ColorSerializer
import com.youngerhousea.miraicompose.utils.ColorsSerializer
import com.youngerhousea.miraicompose.utils.MutableStateSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value


@Serializable
internal class LogColor {
    @SerialName("debug")
    @Serializable(ColorSerializer::class)
    private var _debug: Color = Color(0xCCCCCC)

    @SerialName("verbose")
    @Serializable(ColorSerializer::class)
    private var _verbose: Color = Color(0x00FF00)

//    @SerialName("verbose")
//    @Serializable(MutableStateSerializer::class)
//    private var _verbose: MutableState<@Serializable(ColorSerializer::class) Color> = mutableStateOf(Color(0x00FF00))

    @SerialName("info")
    @Serializable(ColorSerializer::class)
    private var _info: Color = Color(0x00FF00)

    @SerialName("warning")
    @Serializable(ColorSerializer::class)
    private var _warning: Color = Color(0xFFFF00)

    @SerialName("error")
    @Serializable(ColorSerializer::class)
    private var _error: Color = Color(0xFFFF00)

    var debug: Color
        get() = _debug
        set(value) {
            saveSetting { _debug = value }
        }

    var verbose: Color
        get() = _verbose
        set(value) {
            saveSetting { _verbose = value }
        }

    var info: Color
        get() = _info
        set(value) {
            saveSetting { _info = value }
        }

    var warning: Color
        get() = _warning
        set(value) {
            saveSetting { _warning = value }
        }

    var error: Color
        get() = _error
        set(value) {
            saveSetting { _error = value }
        }

}

@Serializable
internal class AppTheme {
    @SerialName("materialLight")
    @Serializable(MutableStateSerializer::class)
    private val _materialLight: MutableState<@Serializable(ColorsSerializer::class) Colors> = mutableStateOf(
        lightColors(
            surface = Color(0xff979595),
            onSurface = Color.White,
            primary = Color(0xf5f5f5f5),
            onPrimary = Color.Black
        )
    )

    val logColors: LogColor = LogColor()

    val materialLight: Colors
        get() = _materialLight.value
}

internal object ComposeSetting : AutoSavePluginConfig("ComposeSetting") {

    val AppTheme: AppTheme by value(AppTheme())
}

private fun saveSetting(block: suspend CoroutineScope.() -> Unit) =
    ComposeDataScope.launch(block = block)






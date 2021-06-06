package com.youngerhousea.miraicompose.core.theme

import com.youngerhousea.miraicompose.core.component.setting.StringColor
import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value


internal object ComposeSetting : AutoSavePluginConfig("ComposeSetting") {
    val AppTheme: AppTheme by value(AppTheme())
}


@Serializable
class AppTheme {
    val logColors: LogColor = LogColor()

    val materialLight = Colors(
        primary = "0xff979595",
        onPrimary = "0xffffffff"
    )
}

@Serializable
class Colors(
    var primary: StringColor = "0xFF6200EE",
    var primaryVariant: StringColor = "0xFF3700B3",
    var secondary: StringColor = "0xFF03DAC6",
    var secondaryVariant: StringColor = "0xFF018786",
    var background: StringColor = "0xFFFFFFFF",
    var surface: StringColor = "0xFFFFFFFF",
    var error: StringColor = "0xFFB00020",
    var onPrimary: StringColor = "0xFFFFFFFF",
    var onSecondary: StringColor = "0xFF000000",
    var onBackground: StringColor = "0xFF000000",
    var onSurface: StringColor = "0xFF000000",
    var onError: StringColor = "0xFFFFFFFF"
)


@Serializable
class LogColor {
    var debug = "0xFF00FFFF"

    var verbose = "0xFFFF00FF"

    var info = "0xFF019d4E"

    var warning = "0xFFf2A111"

    var error = "0xFFEA3C5B"
}



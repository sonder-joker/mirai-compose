package com.youngerhousea.mirai.compose

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.application
import com.youngerhousea.mirai.compose.console.impl.MiraiCompose
import com.youngerhousea.mirai.compose.ui.HostPage
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start

fun main() = application {
    MaterialTheme(colors = color) {
        MiraiComposeWindow(onLoaded = {
            MiraiCompose.start()
        }, onCloseRequest = {
            MiraiCompose.cancel()
            exitApplication()
        }) {
            HostPage()
        }
    }
}


val color = Colors(
    primary = Color(0xFF00b0ff),
    primaryVariant = Color(0xFF69e2ff),
    secondary = Color(0xFF03DAC6),
    secondaryVariant = Color(0xFF018786),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFB00020),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF000000),
    onBackground = Color(0xFF000000),
    onSurface = Color(0xFF000000),
    onError = Color(0xFFFFFFFF),
    isLight = true
)

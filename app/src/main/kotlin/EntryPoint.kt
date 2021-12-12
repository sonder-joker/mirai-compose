package com.youngerhousea.mirai.compose

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.application
import com.youngerhousea.mirai.compose.console.impl.MiraiCompose
import com.youngerhousea.mirai.compose.ui.HostPage
import net.mamoe.mirai.console.ConsoleFrontEndImplementation
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start

fun main() = miraiComposeApplication {
    MiraiComposeWindow(onCloseRequest = ::exitApplication) {
        HostPage()
    }
}

@OptIn(ConsoleFrontEndImplementation::class)
fun miraiComposeApplication(content: @Composable ApplicationScope.() -> Unit) {
    themeApplication {
        content()
        DisposableEffect(Unit) {
            MiraiCompose.start()
            onDispose {
                MiraiCompose.cancel()
            }
        }
    }
}

fun themeApplication(
    content: @Composable ApplicationScope.() -> Unit
) {
    application {
        MaterialTheme(colors = color) {
            content()
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

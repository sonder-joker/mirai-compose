package com.youngerhousea.miraicompose.ui.feature

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.Window
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.IntSize
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.console.MiraiConsoleRepository
import com.youngerhousea.miraicompose.future.Application
import com.youngerhousea.miraicompose.future.ApplicationScope
import com.youngerhousea.miraicompose.theme.ComposeSetting
import com.youngerhousea.miraicompose.theme.ResourceImage
import com.youngerhousea.miraicompose.utils.asComponent
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start
import kotlin.system.exitProcess

fun MiraiComposeView() {
    SetDefaultExceptionHandler()

    Application {
        val compose: MiraiCompose = remember { MiraiCompose() }
        DisposableEffect(Unit) {
            compose.start()
            onDispose {
                compose.cancel()
            }
        }

        if (compose.isReady)
            Ready(compose)
        else
            Loading()

    }
}

private fun SetDefaultExceptionHandler() {
    Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
        ExceptionWithWindows(exception)
    }
}

internal fun ExceptionWithWindows(exception: Throwable) {

    Window(onDismissRequest = {
        exitProcess(1)
    }) {
        SelectionContainer {
            Text(exception.stackTraceToString())
        }
    }
}

@Composable
fun ApplicationScope.Loading() {
    var circleSize by remember { mutableStateOf(50f) }
    val animateCircleSize by animateFloatAsState(circleSize)

    ComposableWindow(
        undecorated = true,
        size = IntSize(400, 400),
    ) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(10)
                circleSize++
                if (circleSize > 200f) {
                    circleSize = 0f
                }
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color(0xffe8e0cb))
        ) {
            drawCircle(Color.Red, radius = animateCircleSize, style = Stroke(1.5f))
            drawCircle(Color.Red, radius = 100f, style = Stroke(1.5f))
            drawCircle(Color.Red, radius = 150f, style = Stroke(1.5f))
        }
    }
}

@Composable
fun ApplicationScope.Ready(compose: MiraiConsoleRepository) {
    ComposableWindow(
        title = "",
        size = IntSize(1280, 768),
        icon = ResourceImage.icon
    ) {
        DesktopMaterialTheme(
            colors = ComposeSetting.AppTheme.materialLight
        ) {
            rememberRootComponent { componentContext ->
                NavHost(componentContext, compose)
            }.asComponent { NavHostUi(it) }()
        }
    }
}
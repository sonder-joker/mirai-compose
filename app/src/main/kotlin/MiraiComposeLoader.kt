package com.youngerhousea.miraicompose.app

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.youngerhousea.miraicompose.app.ui.NavHostUi
import com.youngerhousea.miraicompose.app.utils.ResourceImage
import com.youngerhousea.miraicompose.core.navHost

object MiraiComposeLoader {
    // Compose Entry Point
    @JvmStatic
    fun main(arg: Array<String> = emptyArray()) {
        MiraiComposeView()
    }
}

// 应用起点
@OptIn(ExperimentalComposeUiApi::class)
fun MiraiComposeView() = application {
    // 设置默认处理函数
    ExceptionWindows()
    MiraiComposeWindow()
}


@ExperimentalComposeUiApi
@Composable
fun ApplicationScope.ExceptionWindows(
    onException: (throwable: Throwable) -> Unit = { println(it.stackTraceToString()) },
    state: WindowState = rememberWindowState(size = WindowSize(1280.dp, 768.dp))
) {
    var exception: Throwable? by remember { mutableStateOf(null) }
    var showException: Boolean by remember { mutableStateOf(false) }

    SideEffect {
        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            onException(throwable)
            exception = throwable
            showException = true
        }
    }

    if (showException)
        Window(
            state = state,
            onCloseRequest = ::exitApplication
        ) {
            exception?.let {
                SelectionContainer {
                    Text(it.stackTraceToString())
                }
            }
        }
}


@ExperimentalComposeUiApi
@Composable
private fun ApplicationScope.MiraiComposeWindow(
    state: WindowState = rememberWindowState(size = WindowSize(1280.dp, 768.dp))
) {
    val navHost = rememberRootComponent(factory = ::navHost)

    Window(
        onCloseRequest = ::exitApplication,
        state = state,
        title = "Mirai compose",
        icon = ResourceImage.icon,
    ) {
        DesktopMaterialTheme {
            NavHostUi(navHost)
        }
    }
}


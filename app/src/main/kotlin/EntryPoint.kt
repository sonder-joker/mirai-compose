package com.youngerhousea.mirai.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Maximize
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.youngerhousea.mirai.compose.console.LocalViewModelStore
import com.youngerhousea.mirai.compose.console.Login
import com.youngerhousea.mirai.compose.console.LoginSolverState
import com.youngerhousea.mirai.compose.console.MiraiComposeImplementation
import com.youngerhousea.mirai.compose.console.impl.MiraiComposeImpl
import com.youngerhousea.mirai.compose.ui.ExceptionWindow
import com.youngerhousea.mirai.compose.ui.HostPage
import com.youngerhousea.mirai.compose.ui.login.PicCaptchaDialog
import com.youngerhousea.mirai.compose.ui.login.SliderCaptchaDialog
import com.youngerhousea.mirai.compose.ui.login.UnsafeDeviceLoginVerifyDialog
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start


fun main() = miraiComposeApplication {
    CustomWindows(onCloseRequest = ::exitApplication) {
        HostPage()
    }
}

private inline fun miraiComposeApplication(crossinline content: @Composable ApplicationScope.() -> Unit) {
    val compose = MiraiComposeImpl()
    compose.start()
    exceptionAbleApplication {
        CompositionLocalProvider(LocalMiraiCompose provides compose) {
            CompositionLocalProvider(LocalViewModelStore provides compose.viewModelStore) {
                LoginSolverDialog()
                content()
            }
        }
    }
}

@Suppress("UnnecessaryVariable")
@Composable
private fun LoginSolverDialog() {
    val observeLoginSolverState by LocalMiraiCompose.current.loginSolverState
    when (val loginSolverState = observeLoginSolverState) {
        is LoginSolverState.Nothing -> {
        }
        is LoginSolverState.PicCaptcha ->
            PicCaptchaDialog(loginSolverState) {
                LocalMiraiCompose.current.dispatch(Login.PicCaptcha(it))
            }
        is LoginSolverState.SliderCaptcha -> {
            SliderCaptchaDialog(loginSolverState) {
                LocalMiraiCompose.current.dispatch(Login.SliderCaptcha(it))
            }
        }
        is LoginSolverState.UnsafeDevice -> {
            UnsafeDeviceLoginVerifyDialog(loginSolverState) {
                LocalMiraiCompose.current.dispatch(Login.UnsafeDevice(it))
            }
        }
    }
}

private inline fun exceptionAbleApplication(crossinline content: @Composable ApplicationScope.() -> Unit) {
    val exceptionWindows = MutableStateFlow(false)
    val exceptionMessage: MutableStateFlow<Throwable?> = MutableStateFlow(null)

    Thread.setDefaultUncaughtExceptionHandler { _: Thread, e: Throwable ->
        println(e.stackTraceToString())
        exceptionWindows.value = true
        exceptionMessage.value = e
        if (MiraiConsole.isActive) {
            MiraiConsole.cancel()
        }
    }

    application {
        val isException by exceptionWindows.collectAsState()
        val message by exceptionMessage.collectAsState()
        ExceptionWindow(
            onCloseRequest = ::exitApplication,
            visible = isException,
            message?.stackTraceToString() ?: "No Error Found?"
        )
        content()
    }
}

val LocalMiraiCompose =
    staticCompositionLocalOf<MiraiComposeImplementation> { error("No MiraiComposeImplementation provided") }

@Composable
fun CustomWindows(
    onCloseRequest: () -> Unit,
    state: WindowState = rememberWindowState(),
    visible: Boolean = true,
    title: String = "Untitled",
    icon: Painter? = null,
    enabled: Boolean = true,
    focusable: Boolean = true,
    alwaysOnTop: Boolean = false,
    onPreviewKeyEvent: (KeyEvent) -> Boolean = { false },
    onKeyEvent: (KeyEvent) -> Boolean = { false },
    content: @Composable FrameWindowScope.() -> Unit,
) = Window(
    onCloseRequest,
    state,
    visible,
    title,
    icon,
    undecorated = true,
    resizable = true,
    enabled,
    focusable,
    alwaysOnTop,
    onPreviewKeyEvent,
    onKeyEvent
) {
    Row(
        modifier = Modifier.background(color = Color(75, 75, 75))
            .fillMaxWidth()
            .height(30.dp)
            .padding(start = 20.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        WindowDraggableArea(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Undecorated window", color = Color.White)
        }
        Row {
            Button(
                onClick = {
                    state.isMinimized = true
                }
            ) {
                Icon(Icons.Default.Minimize, null)
            }
            Spacer(modifier = Modifier.width(5.dp))
            Button(
                onClick = {
                    if (state.placement == WindowPlacement.Floating)
                        state.placement = WindowPlacement.Maximized
                    if (state.placement == WindowPlacement.Maximized)
                        state.placement = WindowPlacement.Floating
                }
            ) {
                Icon(Icons.Default.Maximize, null)
            }
            Spacer(modifier = Modifier.width(5.dp))
            Button(
                onClick = onCloseRequest
            ) {
                Icon(Icons.Default.Close, null)
            }
        }
    }
    content()
}


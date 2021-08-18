package com.youngerhousea.mirai.compose

import androidx.compose.runtime.*
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.application
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
    MiraiComposeWindow(onCloseRequest = ::exitApplication) {
        HostPage()
    }
}

private inline fun miraiComposeApplication(crossinline content: @Composable ApplicationScope.() -> Unit) {
    val compose = MiraiComposeImpl()
    compose.start()
    handExceptionApplication {
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

private inline fun handExceptionApplication(crossinline content: @Composable ApplicationScope.() -> Unit) {
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


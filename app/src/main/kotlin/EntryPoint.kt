package com.youngerhousea.mirai.compose

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.application
import com.youngerhousea.mirai.compose.console.LocalViewModelStore
import com.youngerhousea.mirai.compose.console.Login
import com.youngerhousea.mirai.compose.console.LoginSolverState
import com.youngerhousea.mirai.compose.console.MiraiComposeImplementation
import com.youngerhousea.mirai.compose.console.impl.MiraiComposeImpl
import com.youngerhousea.mirai.compose.ui.HostPage
import com.youngerhousea.mirai.compose.ui.login.PicCaptchaDialog
import com.youngerhousea.mirai.compose.ui.login.SliderCaptchaDialog
import com.youngerhousea.mirai.compose.ui.login.UnsafeDeviceLoginVerifyDialog
import net.mamoe.mirai.console.ConsoleFrontEndImplementation
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start

fun main() = miraiComposeApplication {
    MiraiComposeWindow(onCloseRequest = ::exitApplication) {
        HostPage()
    }
}

private inline fun miraiComposeApplication(crossinline content: @Composable ApplicationScope.() -> Unit) {
    val compose = MiraiComposeImpl()
    compose.start()
    themeApplication {
        CompositionLocalProvider(LocalMiraiCompose provides compose) {
            CompositionLocalProvider(LocalViewModelStore provides compose.viewModelStore) {
                LoginSolverDialog()
                content()
            }
        }
    }
}

@Composable
private fun LoginSolverDialog() {
    val observeLoginSolverState by LocalMiraiCompose.current.loginSolverState
    when (observeLoginSolverState) {
        is LoginSolverState.Nothing -> {
        }
        is LoginSolverState.PicCaptcha ->
            PicCaptchaDialog(observeLoginSolverState as LoginSolverState.PicCaptcha) {
                LocalMiraiCompose.current.dispatch(Login.PicCaptcha(it))
            }
        is LoginSolverState.SliderCaptcha -> {
            SliderCaptchaDialog(observeLoginSolverState as LoginSolverState.SliderCaptcha) {
                LocalMiraiCompose.current.dispatch(Login.SliderCaptcha(it))
            }
        }
        is LoginSolverState.UnsafeDevice -> {
            UnsafeDeviceLoginVerifyDialog(observeLoginSolverState as LoginSolverState.UnsafeDevice) {
                LocalMiraiCompose.current.dispatch(Login.UnsafeDevice(it))
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

val LocalMiraiCompose =
    staticCompositionLocalOf<MiraiComposeImplementation> { error("No MiraiComposeImplementation provided") }

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

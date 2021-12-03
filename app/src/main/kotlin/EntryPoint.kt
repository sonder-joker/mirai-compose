package com.youngerhousea.mirai.compose

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.application
import com.youngerhousea.mirai.compose.console.LocalViewModelStore
import com.youngerhousea.mirai.compose.console.Login
import com.youngerhousea.mirai.compose.console.LoginSolverState
import com.youngerhousea.mirai.compose.console.MiraiComposeImplementation
import com.youngerhousea.mirai.compose.console.impl.MiraiCompose
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

@OptIn(ConsoleFrontEndImplementation::class)
private fun miraiComposeApplication(content: @Composable ApplicationScope.() -> Unit) {
    MiraiCompose.start()
    themeApplication {
        CompositionLocalProvider(LocalMiraiCompose provides MiraiCompose) {
            CompositionLocalProvider(LocalViewModelStore provides MiraiCompose.viewModelStore) {
                LoginSolverDialog()
                content()
            }
        }
    }
}

@Composable
private fun LoginSolverDialog() {
    val observeLoginSolverState by MiraiCompose.loginSolverState
    when (observeLoginSolverState) {
        is LoginSolverState.Nothing -> {
        }
        is LoginSolverState.PicCaptcha ->
            PicCaptchaDialog(observeLoginSolverState as LoginSolverState.PicCaptcha) {
                MiraiCompose.dispatch(Login.PicCaptcha(it))
            }
        is LoginSolverState.SliderCaptcha -> {
            SliderCaptchaDialog(observeLoginSolverState as LoginSolverState.SliderCaptcha) {
                MiraiCompose.dispatch(Login.SliderCaptcha(it))
            }
        }
        is LoginSolverState.UnsafeDevice -> {
            UnsafeDeviceLoginVerifyDialog(observeLoginSolverState as LoginSolverState.UnsafeDevice) {
                MiraiCompose.dispatch(Login.UnsafeDevice(it))
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

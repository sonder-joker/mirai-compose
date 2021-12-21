package com.youngerhousea.mirai.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.application
import com.youngerhousea.mirai.compose.console.impl.MiraiCompose
import com.youngerhousea.mirai.compose.resource.color
import com.youngerhousea.mirai.compose.ui.HostPage
import com.youngerhousea.mirai.compose.ui.login.LoginDialog
import kotlinx.coroutines.cancel
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start

fun main() = application {
    MaterialTheme(colors = color) {
        MiraiComposeWindow(onLoaded = {
            Thread {
                MiraiCompose.start()
            }.start()
        }, onCloseRequest = {
            MiraiCompose.cancel()
            exitApplication()
        }) {
            LoginDialog()
            HostPage()
        }
    }
}


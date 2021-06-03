package com.youngerhousea.miraicompose.ui.feature

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.theme.ComposeSetting
import com.youngerhousea.miraicompose.theme.ResourceImage
import com.youngerhousea.miraicompose.utils.asComponent
import kotlinx.coroutines.cancel
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start
import java.awt.datatransfer.UnsupportedFlavorException
import kotlin.system.exitProcess

// 应用起点
@OptIn(ExperimentalComposeUiApi::class)
fun MiraiComposeView() = application {
    // 设置默认处理函数
    val state = rememberWindowState()
    SetDefaultExceptionHandler()
    DisposableEffect(Unit) {
        MiraiCompose.start()
        onDispose {
            MiraiCompose.cancel("Normal Exit")

        }
    }
    Window(
        state = state,
        title = "Mirai compose",
//        size = IntSize(1280, 768),
        icon = ResourceImage.icon,
    ) {
        DesktopMaterialTheme(
            colors = ComposeSetting.AppTheme.materialLight
        ) {
            rememberRootComponent { componentContext ->
                NavHost(componentContext)
            }.asComponent { NavHostUi(it) }()
        }
    }
}


// 取代默认的异常处理
@ExperimentalComposeUiApi
private fun SetDefaultExceptionHandler() {
    Thread.setDefaultUncaughtExceptionHandler { _, exception ->
        if (exception is UnsupportedFlavorException) {
            return@setDefaultUncaughtExceptionHandler
        }
        println(exception.stackTraceToString())
        Window(
            onCloseRequest = {
                exitProcess(1)
            }
        ) {
            SelectionContainer {
                Text(exception.stackTraceToString())
            }
        }
    }
}
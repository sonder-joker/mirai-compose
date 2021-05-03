package com.youngerhousea.miraicompose.ui.feature

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.Window
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.ui.unit.IntSize
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.youngerhousea.miraicompose.future.Application
import com.youngerhousea.miraicompose.theme.ComposeSetting
import com.youngerhousea.miraicompose.theme.ResourceImage
import com.youngerhousea.miraicompose.utils.asComponent
import java.io.PrintStream

// 应用起点
fun MiraiComposeView() {
    // 设置默认处理函数
    SetDefaultExceptionHandler()

    // TODO:加载动画
    Application {
        ComposableWindow(
            title = "Mirai compose",
            size = IntSize(1280, 768),
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
}


// 系统输出
val systemOut: PrintStream = System.out

// 取代默认的异常处理
private fun SetDefaultExceptionHandler() {
    Thread.setDefaultUncaughtExceptionHandler { _, exception ->
        ExceptionWithWindows(exception)
    }
}

// 是否输出日志到 Console
const val DEBUG = true

// 异常窗口
private fun ExceptionWithWindows(exception: Throwable) {
    if (DEBUG)
        systemOut.println(exception.stackTraceToString())
    Window(onDismissRequest = {
//        exitProcess(1)
    }) {
        SelectionContainer {
            Text(exception.stackTraceToString())
        }
    }
}

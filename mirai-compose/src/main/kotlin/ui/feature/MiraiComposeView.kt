package com.youngerhousea.miraicompose.ui.feature

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.Window
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.ui.unit.IntSize
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.theme.ComposeSetting
import com.youngerhousea.miraicompose.theme.ResourceImage
import com.youngerhousea.miraicompose.utils.asComponent
import kotlinx.coroutines.cancel
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start
import java.awt.datatransfer.UnsupportedFlavorException
import java.nio.file.Files
import kotlin.io.path.createTempDirectory
import kotlin.io.path.div

// 应用起点
fun MiraiComposeView() {
    // 设置默认处理函数
    SetDefaultExceptionHandler()
    MiraiCompose.start()
    Window(
        title = "Mirai compose",
        size = IntSize(1280, 768),
        icon = ResourceImage.icon,
        onDismissRequest = {
            MiraiCompose.cancel("Normal Exit")
        }
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
private fun SetDefaultExceptionHandler() {
    Thread.setDefaultUncaughtExceptionHandler { _, exception ->
        if (exception is UnsupportedFlavorException) {
            return@setDefaultUncaughtExceptionHandler
        }
        println(exception.stackTraceToString())
        Window {
            SelectionContainer {
                Text(exception.stackTraceToString())
            }
        }
    }
}
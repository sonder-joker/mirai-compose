package com.youngerhousea.miraicompose.ui.feature

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.youngerhousea.miraicompose.console.AccessibleHolder
import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.future.Application
import com.youngerhousea.miraicompose.future.ApplicationScope
import com.youngerhousea.miraicompose.theme.ComposeSetting
import com.youngerhousea.miraicompose.theme.ResourceImage
import com.youngerhousea.miraicompose.utils.asComponent
import kotlinx.coroutines.cancel
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.PrintStream
import kotlin.system.exitProcess

// 应用起点
fun MiraiComposeView() {
    // 设置默认处理函数
    SetDefaultExceptionHandler()

    val compose = MiraiCompose()

    // 依赖注入
    val module = module {
        single { compose.annotatedLogStorage }
        single(named("ComposeBot")) { compose.botList }
        single(named("Plugin")) { compose.loadedPlugins }
        single<AccessibleHolder> { compose }
        single { MiraiConsole.mainLogger }
        single { ComposeSetting.AppTheme }
    }

    startKoin {
        modules(module)
    }

    Application {
        DisposableEffect(Unit) {
            compose.start()
            onDispose {
                compose.cancel()
            }
        }
        if (compose.already)
            Ready()
        else
            Loading()
    }
}

// TODO:加载动画
@Composable
fun ApplicationScope.Loading() {
    ComposableWindow(
        undecorated = true,
        size = IntSize(400, 400),
    ) {
        Box(Modifier.fillMaxSize().background(Color.DarkGray)) {
        }
    }
}

// Console启动完成后Ui
@Composable
fun ApplicationScope.Ready() {
    ComposableWindow(
        title = "Mirai compose",
        size = IntSize(1280, 768),
        icon = ResourceImage.icon
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

private fun ExceptionWithWindows(exception: Throwable) {
    if (DEBUG)
        systemOut.println(exception.stackTraceToString())
    Window(onDismissRequest = {
        exitProcess(1)
    }) {
        SelectionContainer {
            Text(exception.stackTraceToString())
        }
    }
}

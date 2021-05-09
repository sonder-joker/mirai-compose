package com.youngerhousea.miraicompose.ui.feature

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.Window
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.ui.unit.IntSize
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.youngerhousea.miraicompose.console.AccessibleHolder
import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.console.MiraiComposeRepository
import com.youngerhousea.miraicompose.future.Application
import com.youngerhousea.miraicompose.theme.ComposeSetting
import com.youngerhousea.miraicompose.theme.ResourceImage
import com.youngerhousea.miraicompose.utils.asComponent
import kotlinx.coroutines.cancel
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

// 应用起点
fun MiraiComposeView() {
    // 设置默认处理函数
    SetDefaultExceptionHandler()
    val compose = MiraiCompose()
    compose.start()
    // 依赖注入
    val module = module {
        single(named("ComposeBot")) { compose.botList }
        single(named("Plugin")) { compose.loadedPlugins }
        single<AccessibleHolder> { compose }
        single { ComposeSetting.AppTheme }
        single<MiraiComposeRepository> { compose }
    }
    startKoin {
        modules(module)
    }

    Application {
        ComposableWindow(
            title = "Mirai compose",
            size = IntSize(1280, 768),
            icon = ResourceImage.icon,
            onDismissRequest = {
                compose.cancel("Normal Exit")
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
}

// 取代默认的异常处理
private fun SetDefaultExceptionHandler() {
    Thread.setDefaultUncaughtExceptionHandler { _, exception ->
        println(exception.stackTraceToString())
        Window {
            SelectionContainer {
                Text(exception.stackTraceToString())
            }
        }
    }
}

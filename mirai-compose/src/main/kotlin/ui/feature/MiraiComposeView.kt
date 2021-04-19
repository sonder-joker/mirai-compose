package com.youngerhousea.miraicompose.ui.feature

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.Window
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.IntSize
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.console.debug
import com.youngerhousea.miraicompose.future.Application
import com.youngerhousea.miraicompose.future.ApplicationScope
import com.youngerhousea.miraicompose.theme.ComposeSetting
import com.youngerhousea.miraicompose.theme.ResourceImage
import com.youngerhousea.miraicompose.utils.asComponent
import kotlinx.coroutines.cancel
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.io.PrintStream
import kotlin.system.exitProcess

fun MiraiComposeView() {
    SetDefaultExceptionHandler()


    val module = module {
//        single { parma -> BotChoose(parma.get(), )  }
    }
    startKoin {
        modules(module)
    }

    Application {
        val compose: MiraiCompose = remember { MiraiCompose() }
        DisposableEffect(Unit) {
            compose.start()
            onDispose {
                compose.cancel()
            }
        }
        if (compose.already)
            Ready(compose)
        else
            Loading(compose.annotatedLogStorage)
    }
}

private fun SetDefaultExceptionHandler() {
    Thread.setDefaultUncaughtExceptionHandler { _, exception ->
        ExceptionWithWindows(exception)
    }
}

val systemOut: PrintStream = System.out

private fun ExceptionWithWindows(exception: Throwable) {
    if (debug)
        systemOut.println(exception.stackTraceToString())
    Window(onDismissRequest = {
        exitProcess(1)
    }) {
        SelectionContainer {
            Text(exception.stackTraceToString())
        }
    }
}

@Composable
fun ApplicationScope.Loading(annotatedLogStorage: List<AnnotatedString>) {
    ComposableWindow(
        undecorated = true,
        size = IntSize(400, 400),
    ) {
//        Box(Modifier.fillMaxSize().background(DarkGray)) {
//            annotatedLogStorage.takeIf { it.isNotEmpty() }?.apply { Text(last()) }
//        }
    }
}

@Composable
fun ApplicationScope.Ready(compose: MiraiCompose) {
    ComposableWindow(
        title = "Mirai compose",
        size = IntSize(1280, 768),
        icon = ResourceImage.icon
    ) {
        DesktopMaterialTheme(
            colors = ComposeSetting.AppTheme.materialLight
        ) {
            rememberRootComponent { componentContext ->
                NavHost(componentContext, compose)
            }.asComponent { NavHostUi(it) }()
        }

    }
}
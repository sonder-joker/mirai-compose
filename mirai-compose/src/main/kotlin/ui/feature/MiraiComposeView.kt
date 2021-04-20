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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.IntSize
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.console.MiraiComposeRepository
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

    val compose = MiraiCompose()

    val module = module {
        single<MiraiComposeRepository> { compose }
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
        Box(Modifier.fillMaxSize().background(Color.DarkGray)) {
            annotatedLogStorage.takeIf { it.isNotEmpty() }?.apply { Text(last()) }
        }
    }
}

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
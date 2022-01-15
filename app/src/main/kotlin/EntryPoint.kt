package com.youngerhousea.mirai.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.window.application
import com.youngerhousea.mirai.compose.console.Solver
import com.youngerhousea.mirai.compose.console.impl.MiraiCompose
import com.youngerhousea.mirai.compose.resource.color
import com.youngerhousea.mirai.compose.ui.HostPage
import com.youngerhousea.mirai.compose.ui.login.LoginSolverContent
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start
import java.io.ByteArrayInputStream
import java.util.concurrent.Executors

private val ConsoleThread = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

fun main() {
    startApplication {}
}

fun startApplication(extraAction: () -> Unit) = application {
    val scope = rememberCoroutineScope()
    MaterialTheme(colors = color) {
        MiraiComposeWindow(onLoaded = {
            scope.launch(ConsoleThread) {
                MiraiCompose.start()
                extraAction()
            }
        }, onCloseRequest = {
            MiraiCompose.cancel()
            exitApplication()
        }) {

            val state by MiraiCompose.solverState
            state?.apply {
                MiraiComposeDialog({
                    MiraiCompose.dispatch(Solver.Action.ExitProcessSolver)
                }) {
                    LoginSolverContent(title = "Bot:${bot.id}",
                        tip = title,
                        load = {
                            when (kind) {
                                Solver.Kind.Pic -> Image(
                                    loadImageBitmap(ByteArrayInputStream(data.toByteArray())),
                                    null
                                )
                                Solver.Kind.Slider, Solver.Kind.Unsafe -> {
                                    SelectionContainer {
                                        Text(data)
                                    }
                                }
                            }
                        },
                        onFinish = { MiraiCompose.dispatch(Solver.Action.CompleteVerify(it)) },
                        refresh = { MiraiCompose.dispatch(Solver.Action.RefreshData) },
                        exit = { MiraiCompose.dispatch(Solver.Action.ExitProcessSolver) })
                }
            }

            HostPage()
        }
    }

}


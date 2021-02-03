package com.youngerhousea.miraicompose

import com.youngerhousea.miraicompose.ui.MiraiComposeView
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start
import java.io.File

fun main() {
    configureUserDir()
    MiraiComposeView()
    runCatching { runBlocking { MiraiConsole.job.join() } }
}

internal fun configureUserDir() {
    val projectDir = runCatching {
        File(".")
    }.getOrElse { return }
    if (projectDir.isDirectory) {
        val run = projectDir.resolve("run")
        run.mkdir()
        System.setProperty("user.dir", run.absolutePath)
        println("[Mirai Console] Set user.dir = ${run.absolutePath}")
    }
}
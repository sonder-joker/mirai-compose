package com.youngerhousea.miraicompose

import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start
import java.io.File

fun main() {
    configureUserDir()
    MiraiCompose.start()
}

internal fun configureUserDir() {
    val projectDir = runCatching {
        File(".").resolve("miraicompose")
    }.getOrElse { return }
    if (projectDir.isDirectory) {
        val run = projectDir.resolve("run")

        run.mkdir()
        System.setProperty("user.dir", run.absolutePath)
        println("[Mirai Console] Set user.dir = ${run.absolutePath}")
    }
}



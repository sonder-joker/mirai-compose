package com.youngerhousea.miraicompose

import net.mamoe.mirai.BotFactory
import java.io.File

fun main() {
    configureUserDir()
    MiraiConsoleComposeLoader.main()
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
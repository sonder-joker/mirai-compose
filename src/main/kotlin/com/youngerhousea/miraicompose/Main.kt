package com.youngerhousea.miraicompose

import java.io.File

fun main() {
    configureUserDir()
    MiraiComposeView()
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


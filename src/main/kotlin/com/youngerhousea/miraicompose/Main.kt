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
        val console = projectDir.resolve("console")
        console.mkdir()
        System.setProperty("user.dir", console.absolutePath)
        println("[Mirai Console] Set user.dir = ${console.absolutePath}")
    }
}
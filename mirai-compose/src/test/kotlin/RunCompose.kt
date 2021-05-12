package com.youngerhousea.miraicompose

import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.ui.feature.MiraiComposeView
import java.io.File

fun main() {
    configureUserDir()
    MiraiComposeLoader.main()
}

internal fun configureUserDir() {
    val projectDir = runCatching {
        File(".").resolve("mirai-compose")
    }.getOrElse { return }
    if (projectDir.isDirectory) {
        val run = projectDir.resolve("run")

        run.mkdir()
        System.setProperty("user.dir", run.absolutePath)
        println("[Mirai Console] Set user.dir = ${run.absolutePath}")
    }
}



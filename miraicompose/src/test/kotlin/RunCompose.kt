package com.youngerhousea.miraicompose

import java.io.File
import kotlin.coroutines.AbstractCoroutineContextKey
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.getPolymorphicElement
import kotlin.coroutines.minusPolymorphicKey

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



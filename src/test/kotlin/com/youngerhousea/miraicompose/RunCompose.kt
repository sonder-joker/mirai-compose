package com.youngerhousea.miraicompose

import com.youngerhousea.miraicompose.console.MiraiCompose
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start
import java.io.File

fun main() {
    MiraiCompose.start()
    setSystemOut()
    MiraiComposeView()
}


package com.youngerhousea.miraicompose

import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.ui.feature.MiraiComposeView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start

object MiraiComposeLoader {
    // Compose Entry Point
    @JvmStatic
    fun main(arg:Array<String> = emptyArray()) =
        MiraiComposeView()
}
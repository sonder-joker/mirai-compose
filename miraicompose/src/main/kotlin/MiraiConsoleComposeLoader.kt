package com.youngerhousea.miraicompose

import com.youngerhousea.miraicompose.console.MiraiCompose
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start

object MiraiConsoleComposeLoader {
    @JvmStatic
    fun main(vararg arg: String) {
        MiraiCompose.start()
    }
}


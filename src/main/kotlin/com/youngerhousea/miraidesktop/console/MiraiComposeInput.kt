package com.youngerhousea.miraidesktop.console

import net.mamoe.mirai.console.util.ConsoleInput

object MiraiComposeInput : ConsoleInput {
    override suspend fun requestInput(hint: String): String {
        return ">..."
    }
}


package com.youngerhousea.miraicompose.console

import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.util.ConsoleInput
import net.mamoe.mirai.message.data.Message

internal object MiraiComposeInput : ConsoleInput {
    override suspend fun requestInput(hint: String): String {
        error("Not implementation!")
    }
}

internal object MiraiComposeSender : MiraiConsoleImplementation.ConsoleCommandSenderImpl {
    override suspend fun sendMessage(message: String) {
        println(message)
    }

    override suspend fun sendMessage(message: Message) {
        sendMessage(message.toString())
    }

}

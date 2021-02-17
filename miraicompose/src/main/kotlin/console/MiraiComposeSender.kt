package com.youngerhousea.miraicompose.console

import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.message.data.Message

object MiraiComposeSender : MiraiConsoleImplementation.ConsoleCommandSenderImpl {
    override suspend fun sendMessage(message: String) {
        println(message)
    }

    override suspend fun sendMessage(message: Message) {
        sendMessage(message.toString())
    }

}

package com.youngerhousea.miraicompose.console

import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.command.AbstractCommandSender
import net.mamoe.mirai.console.permission.PermitteeId
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.Message
import kotlin.coroutines.CoroutineContext

//class BotCommandSender(override val bot: Bot) : AbstractCommandSender() {
//
//    override val coroutineContext: CoroutineContext get() = bot.coroutineContext
//
//    override val name: String get() = bot.nick
//
//    override fun toString(): String = name
//
//    override val permitteeId: PermitteeId
//        get() = TODO("Not yet implemented")
//
//    override val subject: Contact? get() = null
//
//    override val user: User? get() = null
//
//    override suspend fun sendMessage(message: String): MessageReceipt<Contact>? = null
//    override suspend fun sendMessage(message: Message): MessageReceipt<Contact>? = null
//
//}
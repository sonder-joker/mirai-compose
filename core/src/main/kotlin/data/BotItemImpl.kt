package com.youngerhousea.miraicompose.core.data

import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.Mirai

class BotItemImpl(
    bot: Bot,
) : BotItem, Bot by bot {
    override val avatar: MutableStateFlow<ByteArray?> = MutableStateFlow(null)

    init {
        bot.launch(Dispatchers.IO) {
            avatar.value = Mirai.Http.get(bot.avatarUrl)
        }
    }
}
package com.youngerhousea.miraicompose.core.data

import kotlinx.coroutines.flow.StateFlow
import net.mamoe.mirai.Bot

interface BotItem : Bot {

    val avatar: StateFlow<ByteArray?>
}
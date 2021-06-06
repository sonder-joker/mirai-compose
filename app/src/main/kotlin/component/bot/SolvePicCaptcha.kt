package com.youngerhousea.miraicompose.component.bot

import com.youngerhousea.miraicompose.ui.bot.ReturnException
import net.mamoe.mirai.Bot

interface SolvePicCaptcha {
    val bot: Bot

    val data: ByteArray

    val result: (String?, ReturnException?) -> Unit
}
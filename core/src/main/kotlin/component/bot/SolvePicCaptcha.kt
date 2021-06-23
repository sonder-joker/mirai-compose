package com.youngerhousea.miraicompose.core.component.bot

import net.mamoe.mirai.Bot

interface SolvePicCaptcha {
    val bot: Bot

    val data: ByteArray

    fun onSuccess(data:String)

    fun onExcept()

}
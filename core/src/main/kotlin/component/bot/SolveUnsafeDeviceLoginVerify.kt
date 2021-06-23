package com.youngerhousea.miraicompose.core.component.bot

import net.mamoe.mirai.Bot

interface SolveUnsafeDeviceLoginVerify {
    val bot: Bot

    fun onExcept()

    fun onSuccess()

    val qrCodeUrl: String
}

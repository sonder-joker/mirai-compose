package com.youngerhousea.miraicompose.core.component.bot

import net.mamoe.mirai.Bot

interface SolveUnsafeDeviceLoginVerify {
    val bot: Bot

    val result: (String?, ReturnException?) -> Unit

    val qrCodeUrl: String
}

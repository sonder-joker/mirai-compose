package com.youngerhousea.miraicompose.component.bot

import com.youngerhousea.miraicompose.ui.bot.ReturnException
import net.mamoe.mirai.Bot

interface SolveUnsafeDeviceLoginVerify {
    val bot: Bot

    val result: (String?, ReturnException?) -> Unit

    val qrCodeUrl: String
}

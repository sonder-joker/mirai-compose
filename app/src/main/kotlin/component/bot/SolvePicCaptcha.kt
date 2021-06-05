package com.youngerhousea.miraicompose.component.bot

import androidx.compose.ui.graphics.ImageBitmap
import com.youngerhousea.miraicompose.ui.bot.ReturnException
import net.mamoe.mirai.Bot

interface SolvePicCaptcha {
    val bot: Bot

    val imageBitmap: ImageBitmap

    val result: (String?, ReturnException?) -> Unit
}
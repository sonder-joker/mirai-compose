package com.youngerhousea.miraicompose.component.impl.bot

import androidx.compose.ui.graphics.ImageBitmap
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.component.bot.SolvePicCaptcha
import com.youngerhousea.miraicompose.ui.bot.ReturnException
import net.mamoe.mirai.Bot

class SolvePicCaptchaImpl(
    context: ComponentContext,
    override val bot: Bot,
    override val imageBitmap: ImageBitmap,
    override val result: (String?, ReturnException?) -> Unit
) : SolvePicCaptcha, ComponentContext by context
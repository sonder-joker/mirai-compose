package com.youngerhousea.miraicompose.core.component.impl.bot

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.core.component.bot.ReturnException
import com.youngerhousea.miraicompose.core.component.bot.SolvePicCaptcha
import net.mamoe.mirai.Bot

internal class SolvePicCaptchaImpl(
    context: ComponentContext,
    override val bot: Bot,
    override val data: ByteArray,
    inline val result: (String?, ReturnException?) -> Unit
) : SolvePicCaptcha, ComponentContext by context {
    override fun onSuccess(data: String) {
        result(data, null)
    }

    override fun onExcept() {
        result(null, ReturnException())
    }
}
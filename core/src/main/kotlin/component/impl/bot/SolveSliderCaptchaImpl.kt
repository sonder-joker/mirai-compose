package com.youngerhousea.miraicompose.core.component.impl.bot

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.core.component.bot.ReturnException
import com.youngerhousea.miraicompose.core.component.bot.SolveSliderCaptcha
import net.mamoe.mirai.Bot


class SolveSliderCaptchaImpl(
    context: ComponentContext,
    val bot: Bot,
    override val url: String,
    val result: (String?, ReturnException?) -> Unit
) : SolveSliderCaptcha, ComponentContext by context


package com.youngerhousea.miraicompose.component.impl.bot

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.component.bot.SolveSliderCaptcha
import com.youngerhousea.miraicompose.ui.bot.ReturnException
import net.mamoe.mirai.Bot


class SolveSliderCaptchaImpl(
    context: ComponentContext,
    val bot: Bot,
    override val url: String,
    val result: (String?, ReturnException?) -> Unit
) : SolveSliderCaptcha, ComponentContext by context


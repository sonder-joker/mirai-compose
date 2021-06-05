package com.youngerhousea.miraicompose.component.impl.bot

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.component.bot.Message
import com.youngerhousea.miraicompose.console.ComposeBot

class MessageImpl(
    componentContext: ComponentContext,
    override val botList: List<ComposeBot>
) : Message, ComponentContext by componentContext {
}
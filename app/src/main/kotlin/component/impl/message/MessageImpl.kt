package com.youngerhousea.miraicompose.component.impl.message

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.component.message.Message
import com.youngerhousea.miraicompose.console.ComposeBot

class MessageImpl(
    componentContext: ComponentContext,
    override val botList: List<ComposeBot>
) : Message, ComponentContext by componentContext {
}
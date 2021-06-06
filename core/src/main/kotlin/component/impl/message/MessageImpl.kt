package com.youngerhousea.miraicompose.core.component.impl.message

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.core.component.message.Message
import com.youngerhousea.miraicompose.core.console.ComposeBot

class MessageImpl(
    componentContext: ComponentContext,
    override val botList: List<ComposeBot>
) : Message, ComponentContext by componentContext {
}
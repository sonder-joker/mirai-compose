package com.youngerhousea.miraicompose.core.component.impl.message

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.core.component.message.Message
import net.mamoe.mirai.Bot

internal class MessageImpl(
    componentContext: ComponentContext,
    override val botList: List<Bot>
) : Message, ComponentContext by componentContext
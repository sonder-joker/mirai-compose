package com.youngerhousea.miraicompose.core.component.impl.log

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.core.component.log.ConsoleLog
import com.youngerhousea.miraicompose.core.console.ComposeLog
import net.mamoe.mirai.utils.MiraiLogger

class ConsoleLogImpl(
    componentContext: ComponentContext,
    override val loggerStorage: List<ComposeLog>,
    override val logger: MiraiLogger
) : ConsoleLog, ComponentContext by componentContext
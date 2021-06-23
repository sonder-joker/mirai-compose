package com.youngerhousea.miraicompose.core.component.impl.log

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.core.component.log.ConsoleLog
import com.youngerhousea.miraicompose.core.console.ComposeLog
import com.youngerhousea.miraicompose.core.utils.getValue
import com.youngerhousea.miraicompose.core.utils.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import net.mamoe.mirai.utils.MiraiLogger

internal class ConsoleLogImpl(
    componentContext: ComponentContext,
    loggerStorage: List<ComposeLog>,
    override val logger: MiraiLogger
) : ConsoleLog, ComponentContext by componentContext {
    override val model = MutableStateFlow(ConsoleLog.Model(loggerStorage, ""))

    var delegateModel by model

    override fun setSearchContent(content: String) {
        delegateModel = delegateModel.copy(searchContent = content)
    }

}
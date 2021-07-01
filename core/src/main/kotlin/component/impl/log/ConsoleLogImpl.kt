package com.youngerhousea.miraicompose.core.component.impl.log

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.core.component.log.ConsoleLog
import com.youngerhousea.miraicompose.core.console.Log
import com.youngerhousea.miraicompose.core.utils.componentScope
import com.youngerhousea.miraicompose.core.utils.getValue
import com.youngerhousea.miraicompose.core.utils.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import net.mamoe.mirai.utils.MiraiLogger

internal class ConsoleLogImpl(
    componentContext: ComponentContext,
    val log: List<Log>,
    override val logger: MiraiLogger
) : ConsoleLog, ComponentContext by componentContext, CoroutineScope by componentContext.componentScope() {

    override val model = MutableStateFlow(ConsoleLog.Model("", log))

    var delegateModel by model

    override fun setSearchContent(content: String) {
        delegateModel = delegateModel.copy(searchContent = content)
    }

}
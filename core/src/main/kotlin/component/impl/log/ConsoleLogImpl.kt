package com.youngerhousea.miraicompose.core.component.impl.log

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.core.component.log.ConsoleLog
import com.youngerhousea.miraicompose.core.console.ComposeLog
import com.youngerhousea.miraicompose.core.utils.componentScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.mamoe.mirai.utils.MiraiLogger

internal class ConsoleLogImpl(
    componentContext: ComponentContext,
    override val loggerStorage: StateFlow<ComposeLog>,
    override val logger: MiraiLogger
) : ConsoleLog, ComponentContext by componentContext {
    private val scope = componentScope()

    override val searchContent = MutableStateFlow("")

    override fun setSearchContent(content: String) {
        scope.launch {
            searchContent.emit(content)
        }
    }

}

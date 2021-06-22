package com.youngerhousea.miraicompose.core.component.log

import com.youngerhousea.miraicompose.core.console.ComposeLog
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import net.mamoe.mirai.utils.MiraiLogger


/**
 * Compose的所有日志
 *
 */
interface ConsoleLog {
    val loggerStorage: StateFlow<ComposeLog>

    val logger: MiraiLogger

    val searchContent: StateFlow<String>

    fun setSearchContent(content: String)
}
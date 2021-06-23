package com.youngerhousea.miraicompose.core.component.log

import com.youngerhousea.miraicompose.core.console.ComposeLog
import kotlinx.coroutines.flow.StateFlow
import net.mamoe.mirai.utils.MiraiLogger


/**
 * Compose的所有日志
 *
 */
interface ConsoleLog {
    data class Model (
        val loggerStorage: List<ComposeLog>,
        val searchContent: String
    )

    val model:StateFlow<Model>

    val logger: MiraiLogger

    fun setSearchContent(content: String)
}
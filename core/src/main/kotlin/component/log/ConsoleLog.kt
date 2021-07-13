package com.youngerhousea.miraicompose.core.component.log

import com.youngerhousea.miraicompose.core.console.Log
import kotlinx.coroutines.flow.StateFlow
import net.mamoe.mirai.utils.MiraiLogger


/**
 * Compose的所有日志
 *
 */
interface ConsoleLog {
    data class Model (
        val searchContent: String,
        val log:List<Log>,
        val command:String
    )

    val model:StateFlow<Model>

    fun setSearchContent(content: String)

    fun setCurrentCommand(content: String)

    fun onSearchClick()
}
package com.youngerhousea.miraicompose.core.component.log

import com.youngerhousea.miraicompose.core.console.Log
import com.youngerhousea.miraicompose.core.data.LogColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.mamoe.mirai.utils.MiraiLogger


/**
 * Compose的所有日志
 *
 */
interface ConsoleLog {

    fun setSearchContent(content: String)

    fun setCurrentCommand(content: String)

    fun onSearchClick()

    val log:StateFlow<List<Log>>

    val searchContent: StateFlow<String>

    val command: StateFlow<String>

    val logColor: StateFlow<LogColor>
}



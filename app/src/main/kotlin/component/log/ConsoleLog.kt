package com.youngerhousea.miraicompose.component.log

import com.youngerhousea.miraicompose.console.ComposeLog
import net.mamoe.mirai.utils.MiraiLogger


/**
 * Compose的所有日志
 *
 */
interface ConsoleLog {
    val loggerStorage: List<ComposeLog>

    val logger: MiraiLogger
}
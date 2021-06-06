package com.youngerhousea.miraicompose.core.component.log

import com.youngerhousea.miraicompose.core.console.ComposeLog
import net.mamoe.mirai.utils.MiraiLogger


/**
 * Compose的所有日志
 *
 */
interface ConsoleLog {
    val loggerStorage: List<ComposeLog>

    val logger: MiraiLogger
}
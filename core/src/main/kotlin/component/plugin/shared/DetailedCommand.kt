package com.youngerhousea.miraicompose.core.component.plugin.shared

import net.mamoe.mirai.console.command.Command

/**
 *  插件指令
 */
interface DetailedCommand {
    val commands: List<Command>
}
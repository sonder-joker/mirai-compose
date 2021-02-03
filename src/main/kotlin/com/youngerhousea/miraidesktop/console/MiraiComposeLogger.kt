
package com.youngerhousea.miraidesktop.console

import net.mamoe.mirai.utils.MiraiInternalApi
import net.mamoe.mirai.utils.PlatformLogger


private val LoggerStorage = ArrayList<String>()

@OptIn(MiraiInternalApi::class)
object MiraiComposeLogger
    : PlatformLogger("MiraiCompose", {
    LoggerStorage.add(it)
    println(it)
}) {
    private val logs: MutableList<String> get() = LoggerStorage
    fun clearLog() = logs.clear()
}

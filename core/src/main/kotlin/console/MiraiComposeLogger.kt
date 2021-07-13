package com.youngerhousea.miraicompose.core.console

import net.mamoe.mirai.utils.MiraiLoggerPlatformBase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

enum class ANSI(
    val value:String
) {
    RESET("\u001B[0m"),
    BLACK ("\u001B[30m"),
    RED ("\u001B[31m"),
    GREEN ("\u001B[32m"),
    YELLOW ( "\u001B[33m"),
    BLUE ("\u001B[34m"),
    PURPLE ("\u001B[35m"),
    CYAN ("\u001B[36m"),
    WHITE("\u001B[37m")
}

data class Log(
    val logPriority: LogPriority,
    val message: String?,
    val throwable: Throwable?,
    val identity: String?
)

val Log.compositionLog: String
    get() {
        val message = if (throwable != null)
            message ?: throwable.toString() + "\n${throwable.stackTraceToString()}"
        else
            message.toString()
        return "$currentTimeFormatted ${logPriority.simpleName}/$identity: $message"
    }

private val timeFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE)

private val currentTimeFormatted get() = timeFormat.format(Date())

/**
 *  默认Logger实现
 */
class MiraiComposeLogger(
    override val identity: String?,
    val printLog: (message: String?, throwable: Throwable?, priority: LogPriority) -> Unit
) : MiraiLoggerPlatformBase() {

    public override fun verbose0(message: String?, e: Throwable?) =
        printLog(message, e, LogPriority.VERBOSE)

    public override fun info0(message: String?, e: Throwable?) =
        printLog(message, e, LogPriority.INFO)

    public override fun warning0(message: String?, e: Throwable?) =
        printLog(message, e, LogPriority.WARNING)

    public override fun error0(message: String?, e: Throwable?) =
        printLog(message, e, LogPriority.ERROR)

    public override fun debug0(message: String?, e: Throwable?) =
        printLog(message, e, LogPriority.DEBUG)

}

enum class LogPriority(
    val simpleName: String
) {
    VERBOSE("V"),
    INFO("I"),
    WARNING("W"),
    ERROR("E"),
    DEBUG("D")
}

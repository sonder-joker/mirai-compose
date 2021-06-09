package com.youngerhousea.miraicompose.core.console

import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.utils.MiraiLoggerPlatformBase
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.io.path.createFile
import kotlin.io.path.div


private val logTimeFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE)
private inline val ComposeLog.currentDate: String get() = logTimeFormat.format(Date())

/**
 * Compose log
 *
 * 单个的日志
 * @property priority
 * @property identity
 * @property message
 * @constructor Create empty Compose log
 */
class ComposeLog(
    val priority: LogPriority,
    identity: String?,
    message: String
) {
    val original = "$currentDate ${priority.simpleName}/$identity: $message"

    companion object {
        internal val storage: MutableList<ComposeLog> = mutableListOf()
    }
}


private const val ANSI_RESET = "\u001B[0m"
private const val ANSI_BLACK = "\u001B[30m"
private const val ANSI_RED = "\u001B[31m"
private const val ANSI_GREEN = "\u001B[32m"
private const val ANSI_YELLOW = "\u001B[33m"
private const val ANSI_BLUE = "\u001B[34m"
private const val ANSI_PURPLE = "\u001B[35m"
private const val ANSI_CYAN = "\u001B[36m"
private const val ANSI_WHITE = "\u001B[37m"

val ComposeLog.consoleText
    get(): String = when (priority) {
        LogPriority.VERBOSE -> original
        LogPriority.DEBUG -> original
        LogPriority.INFO -> ANSI_GREEN + original + ANSI_RESET
        LogPriority.WARNING -> ANSI_YELLOW + original + ANSI_RESET
        LogPriority.ERROR -> ANSI_RED + original + ANSI_RESET
    }

interface LogPerform<T> {
    fun <T> loggerKind(text: String): T

}

internal val MiraiConsole.logPath get() = rootPath / "log"

private val fileNameFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.SIMPLIFIED_CHINESE)
private val fileName = (MiraiConsole.logPath / fileNameFormat.format(Date())).createFile()

sealed class Kind{
    object Default:Kind()
}

val Default = Kind.Default


interface LoggerStorageOwner {

    val kind: Kind get() = Default

    fun <T> loggerKind(text: String): T
}

/**
 * [MiraiCompose] 默认Logger实现
 */
class MiraiComposeLogger(
    override val identity: String?
) : MiraiLoggerPlatformBase() {

    private fun printLog(message: String?, priority: LogPriority) {
        if (message != null) {
            val compose = ComposeLog(priority, identity, message)
            println(compose.consoleText)
            ComposeLog.storage.add(compose)
            Files.write(fileName, (message + "\n").toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.APPEND)
        }
    }

    public override fun verbose0(message: String?): Unit = printLog(message, LogPriority.VERBOSE)
    public override fun verbose0(message: String?, e: Throwable?) {
        if (e != null) verbose((message ?: e.toString()) + "\n${e.stackTraceToString()}")
        else verbose(message.toString())
    }

    public override fun info0(message: String?): Unit = printLog(message, LogPriority.INFO)
    public override fun info0(message: String?, e: Throwable?) {
        if (e != null) info((message ?: e.toString()) + "\n${e.stackTraceToString()}")
        else info(message.toString())
    }

    public override fun warning0(message: String?): Unit = printLog(message, LogPriority.WARNING)
    public override fun warning0(message: String?, e: Throwable?) {
        if (e != null) warning((message ?: e.toString()) + "\n${e.stackTraceToString()}")
        else warning(message.toString())
    }

    public override fun error0(message: String?): Unit = printLog(message, LogPriority.ERROR)
    public override fun error0(message: String?, e: Throwable?) {
        if (e != null) error((message ?: e.toString()) + "\n${e.stackTraceToString()}")
        else error(message.toString())
    }

    public override fun debug0(message: String?): Unit = printLog(message, LogPriority.DEBUG)
    public override fun debug0(message: String?, e: Throwable?) {
        if (e != null) debug((message ?: e.toString()) + "\n${e.stackTraceToString()}")
        else debug(message.toString())
    }
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

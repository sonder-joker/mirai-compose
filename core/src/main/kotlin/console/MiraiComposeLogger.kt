package com.youngerhousea.miraicompose.core.console

import com.youngerhousea.miraicompose.core.utils.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.utils.MiraiLoggerPlatformBase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.div


private val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")


/**
 * Compose log
 *
 * 单个的日志
 * @property priority
 * @property identity
 * @property message
 * @constructor Create empty Compose log
 */
data class ComposeLog(
    val priority: LogPriority,
    val identity: String?,
    val message: String
) {

    val original = "${LocalDateTime.now().format(format)} ${priority.simpleName}/$identity: $message"

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

internal val MiraiConsole.logPath get() = (rootPath / "log").createDirectories()

private val fileNameFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")
private val fileName = (MiraiConsole.logPath / LocalDateTime.now().format(fileNameFormat)).createFile()

//println(compose.consoleText)
//Files.write(fileName, (message + "\n").toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.APPEND)


/**
 * [MiraiCompose] 默认Logger实现
 */
class MiraiComposeLogger(
    override val identity: String?
) : MiraiLoggerPlatformBase() {

    companion object {
        private val _storage = MutableStateFlow(
            mutableListOf(
                ComposeLog(
                    LogPriority.INFO,
                    null,
                    ""
                )
            )
        )

        val storage: List<ComposeLog> by _storage
    }

    private fun printLog(message: String?, priority: LogPriority) {
        if (message != null) {
            val list = _storage.value
            list.add(ComposeLog(priority, identity, message))
            _storage.value = list
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

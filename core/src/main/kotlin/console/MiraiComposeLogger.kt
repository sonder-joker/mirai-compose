package com.youngerhousea.miraicompose.core.console

import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.utils.MiraiLoggerPlatformBase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.div


private val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")


private const val ANSI_RESET = "\u001B[0m"
private const val ANSI_BLACK = "\u001B[30m"
private const val ANSI_RED = "\u001B[31m"
private const val ANSI_GREEN = "\u001B[32m"
private const val ANSI_YELLOW = "\u001B[33m"
private const val ANSI_BLUE = "\u001B[34m"
private const val ANSI_PURPLE = "\u001B[35m"
private const val ANSI_CYAN = "\u001B[36m"
private const val ANSI_WHITE = "\u001B[37m"

typealias Log = Triple<LogPriority, String?, Throwable?>

//val ComposeLog.consoleText
//    get(): String = when (priority) {
//        LogPriority.VERBOSE -> original
//        LogPriority.DEBUG -> original
//        LogPriority.INFO -> ANSI_GREEN + original + ANSI_RESET
//        LogPriority.WARNING -> ANSI_YELLOW + original + ANSI_RESET
//        LogPriority.ERROR -> ANSI_RED + original + ANSI_RESET
//    }

internal val MiraiConsole.logPath get() = (rootPath / "log").createDirectories()

private val fileNameFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")
private val fileName = (MiraiConsole.logPath / LocalDateTime.now().format(fileNameFormat)).createFile()

//println(compose.consoleText)
//Files.write(fileName, (message + "\n").toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.APPEND)

val Log.original: String get() = "${first.simpleName} / $second${third?.let { "\n${it.message}" }}"

/**
 * [MiraiCompose] 默认Logger实现
 */
class MiraiComposeLogger(
    override val identity: String?,
    val printLog:(message: String?, throwable: Throwable?, priority: LogPriority) -> Unit
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

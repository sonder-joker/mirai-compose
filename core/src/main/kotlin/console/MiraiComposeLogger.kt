package com.youngerhousea.miraicompose.core.console

import net.mamoe.mirai.utils.MiraiLoggerPlatformBase
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.io.path.createFile
import kotlin.io.path.div


private val logTimeFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE)
private inline val currentDate: String get() = logTimeFormat.format(Date())

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
    val priority: MiraiComposeLogger.LogPriority,
    identity: String?,
    message: String
) {
    val text = "$currentDate ${priority.simpleName}/$identity: $message"

    companion object {
        internal val storage: MutableList<ComposeLog> = /*TODO:mutableStateListOf()*/ mutableListOf()
    }
}


/**
 * [MiraiCompose] 默认Logger实现
 */
class MiraiComposeLogger(
    override val identity: String?
) : MiraiLoggerPlatformBase() {

    companion object {
        private val logTimeFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.SIMPLIFIED_CHINESE)
        private val currentDate: String = logTimeFormat.format(Date())
        private val fileName = (MiraiCompose.logPath / currentDate).createFile()
    }

    private fun printLog(message: String?, priority: LogPriority) {
        if (message != null) {
            val compose = ComposeLog(priority, identity, message)
            println(compose.text)
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

    enum class LogPriority(
        val simpleName: String
    ) {
        VERBOSE("V"),
        INFO("I"),
        WARNING("W"),
        ERROR("E"),
        DEBUG("D")
    }
}


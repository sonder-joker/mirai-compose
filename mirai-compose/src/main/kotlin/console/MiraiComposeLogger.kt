package com.youngerhousea.miraicompose.console

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.youngerhousea.miraicompose.theme.ComposeSetting
import net.mamoe.mirai.utils.MiraiLoggerPlatformBase
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.PatternSyntaxException
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
    private inline val ANSI_RESET get() = "\u001B[0m"
    private inline val ANSI_BLACK get() = "\u001B[30m"
    private inline val ANSI_RED get() = "\u001B[31m"
    private inline val ANSI_GREEN get() = "\u001B[32m"
    private inline val ANSI_YELLOW get() = "\u001B[33m"
    private inline val ANSI_BLUE get() = "\u001B[34m"
    private inline val ANSI_PURPLE get() = "\u001B[35m"
    private inline val ANSI_CYAN get() = "\u001B[36m"
    private inline val ANSI_WHITE get() = "\u001B[37m"


    val text = "$currentDate ${priority.simpleName}/$identity: $message"

    val color: Color
        get() = when (priority) {
            MiraiComposeLogger.LogPriority.VERBOSE -> ComposeSetting.AppTheme.logColors.verbose
            MiraiComposeLogger.LogPriority.INFO -> ComposeSetting.AppTheme.logColors.info
            MiraiComposeLogger.LogPriority.WARNING -> ComposeSetting.AppTheme.logColors.warning
            MiraiComposeLogger.LogPriority.ERROR -> ComposeSetting.AppTheme.logColors.error
            MiraiComposeLogger.LogPriority.DEBUG -> ComposeSetting.AppTheme.logColors.debug
        }

    fun parseInConsole(): String = when (priority) {
        MiraiComposeLogger.LogPriority.VERBOSE -> text
        MiraiComposeLogger.LogPriority.DEBUG -> text
        MiraiComposeLogger.LogPriority.INFO -> ANSI_GREEN + text + ANSI_RESET
        MiraiComposeLogger.LogPriority.WARNING -> ANSI_YELLOW + text + ANSI_RESET
        MiraiComposeLogger.LogPriority.ERROR -> ANSI_RED + text + ANSI_RESET
    }


    fun parseInCompose(): AnnotatedString {
        return buildAnnotatedString {
            pushStyle(SpanStyle(color))
            append(text)
        }
    }

    fun parseInSearch(searchText: String): AnnotatedString {
        if (searchText.isEmpty()) return parseInCompose()
        val builder = AnnotatedString.Builder()
        try {
            text.split("((?<=${searchText})|(?=${searchText}))".toRegex()).forEach {
                if (it == searchText)
                    builder.append(
                        AnnotatedString(
                            it,
                            spanStyle = SpanStyle(background = Color.Yellow),
                        )
                    )
                else
                    builder.append(
                        AnnotatedString(
                            it,
                            spanStyle = SpanStyle(color),
                        )
                    )

            }
        } catch (e: PatternSyntaxException) {
            //TODO:
            return parseInCompose()
        }
        return builder.toAnnotatedString()
    }

    companion object {
        internal val storage: MutableList<ComposeLog> = mutableStateListOf()
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
            println(compose.parseInConsole())
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


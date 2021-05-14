package com.youngerhousea.miraicompose.console

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.youngerhousea.miraicompose.theme.ComposeSetting
import net.mamoe.mirai.utils.MiraiLoggerPlatformBase
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


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

    private val logTimeFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE)

    private inline val currentDate: String get() = logTimeFormat.format(Date())

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
        if(searchText.isEmpty()) return parseInCompose()
        val builder = AnnotatedString.Builder()
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
        return builder.toAnnotatedString()
    }

    companion object {
        internal val logStorage: MutableList<ComposeLog> = mutableStateListOf()
    }
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
            println(compose.parseInConsole())
            ComposeLog.logStorage.add(compose)
        }
    }

    public override fun verbose0(message: String?): Unit = printLog(message, LogPriority.VERBOSE)
    public override fun verbose0(message: String?, e: Throwable?) {
        if (e != null) verbose((message ?: e.toString()) + "\n${e.stackTraceString}")
        else verbose(message.toString())
    }

    public override fun info0(message: String?): Unit = printLog(message, LogPriority.INFO)
    public override fun info0(message: String?, e: Throwable?) {
        if (e != null) info((message ?: e.toString()) + "\n${e.stackTraceString}")
        else info(message.toString())
    }

    public override fun warning0(message: String?): Unit = printLog(message, LogPriority.WARNING)
    public override fun warning0(message: String?, e: Throwable?) {
        if (e != null) warning((message ?: e.toString()) + "\n${e.stackTraceString}")
        else warning(message.toString())
    }

    public override fun error0(message: String?): Unit = printLog(message, LogPriority.ERROR)
    public override fun error0(message: String?, e: Throwable?) {
        if (e != null) error((message ?: e.toString()) + "\n${e.stackTraceString}")
        else error(message.toString())
    }

    public override fun debug0(message: String?): Unit = printLog(message, LogPriority.DEBUG)
    public override fun debug0(message: String?, e: Throwable?) {
        if (e != null) debug((message ?: e.toString()) + "\n${e.stackTraceString}")
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

internal class BufferedOutputStream constructor(
    private val size: Int = 1024 * 1024,
    private val logger: (String?) -> Unit
) : ByteArrayOutputStream() {
    override fun write(b: Int) {
        if (this.count >= size) {
            flush()
        }
        if (b == 10) {
            flush()
        } else {
            super.write(b)
        }
    }

    override fun write(b: ByteArray) {
        write(b, 0, b.size)
    }

    private fun ByteArray.findSplitter(off: Int, end: Int): Int {
        var o = off
        while (o < end) {
            if (get(o) == 10.toByte()) {
                return o
            }
            o++
        }
        return -1
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        val ed = off + len
        if (ed > b.size || ed < 0) {
            throw ArrayIndexOutOfBoundsException()
        }
        write0(b, off, ed)
    }

    private fun write0(b: ByteArray, off: Int, end: Int) {
        val size = end - off
        if (size < 1) return
        val spl = b.findSplitter(off, end)
        if (spl == -1) {
            val over = this.size - (size + count)
            if (over < 0) {
                write0(b, off, end + over)
                flush()
                write0(b, off - over, end)
            } else {
                super.write(b, off, size)
            }
        } else {
            write0(b, off, spl)
            flush()
            write0(b, spl + 1, end)
        }
    }

    override fun writeTo(out: OutputStream?) {
        throw UnsupportedOperationException()
    }

    override fun flush() {
        logger(String(buf, 0, count, Charsets.UTF_8))
        count = 0
    }
}

@get:JvmSynthetic
internal val Throwable.stackTraceString
    get() = this.stackTraceToString()

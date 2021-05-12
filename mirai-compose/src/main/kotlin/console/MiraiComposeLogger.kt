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

private val logTimeFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE)

internal val annotatedLogStorage: MutableList<AnnotatedString> = mutableStateListOf()

/**
 * [MiraiCompose] 默认Logger实现
 */
class MiraiComposeLogger(
    override val identity: String?
) : MiraiLoggerPlatformBase() {
    private inline val currentDate: String get() = logTimeFormat.format(Date())

    private val LogPriority.color: Color
        get() = when (this) {
            LogPriority.VERBOSE -> ComposeSetting.AppTheme.logColors.verbose
            LogPriority.INFO -> ComposeSetting.AppTheme.logColors.info
            LogPriority.WARNING -> ComposeSetting.AppTheme.logColors.warning
            LogPriority.ERROR -> ComposeSetting.AppTheme.logColors.error
            LogPriority.DEBUG -> ComposeSetting.AppTheme.logColors.debug
        }

    private fun printLog(message: String?, priority: LogPriority) {
        if (message != null) {
            val annotatedLog =
                annotatedString(priority, message)
            println(priority.parseInConsole(annotatedLog.text))
            annotatedLogStorage.add(annotatedLog)
        }
    }

    private fun annotatedString(
        priority: LogPriority,
        message: String?
    ) = buildAnnotatedString {
        pushStyle(SpanStyle(priority.color))
        append("$currentDate ${priority.simpleName}/$identity: $message")
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

    private fun LogPriority.parseInConsole(text: String): String =
        when (this) {
            LogPriority.VERBOSE -> text
            LogPriority.DEBUG -> text
            LogPriority.INFO -> ANSI_GREEN + text + ANSI_RESET
            LogPriority.WARNING -> ANSI_YELLOW + text + ANSI_RESET
            LogPriority.ERROR -> ANSI_RED + text + ANSI_RESET
        }
}


const val ANSI_RESET = "\u001B[0m"
const val ANSI_BLACK = "\u001B[30m"
const val ANSI_RED = "\u001B[31m"
const val ANSI_GREEN = "\u001B[32m"
const val ANSI_YELLOW = "\u001B[33m"
const val ANSI_BLUE = "\u001B[34m"
const val ANSI_PURPLE = "\u001B[35m"
const val ANSI_CYAN = "\u001B[36m"
const val ANSI_WHITE = "\u001B[37m"

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

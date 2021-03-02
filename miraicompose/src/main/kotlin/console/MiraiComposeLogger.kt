package com.youngerhousea.miraicompose.console

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.theme.AppTheme
import net.mamoe.mirai.utils.MiraiLoggerPlatformBase
import net.mamoe.mirai.utils.SimpleLogger
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


val LoggerStorage = mutableStateListOf<AnnotatedString>()

val ComposeBot.logs get() = LoggerStorage.filter { it.text.contains("Bot.${this.id}") }

private val timeFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.SIMPLIFIED_CHINESE)
private val currentTimeFormatted: String get() = timeFormat.format(Date())

internal fun writeToFile(it: String) {
    Files.write(
        MiraiCompose.logFiles.resolve(
            "$currentTimeFormatted.txt"
        ),
        "$it\n".toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.APPEND
    )
}

class MiraiComposeLogger(override val identity: String?) : MiraiLoggerPlatformBase() {

    private val currentTimeFormatted: String get() = timeFormat.format(Date())

    private val SimpleLogger.LogPriority.color: Color
        get() = when (this) {
            SimpleLogger.LogPriority.VERBOSE -> AppTheme.LogColor.verbose
            SimpleLogger.LogPriority.INFO -> AppTheme.LogColor.info
            SimpleLogger.LogPriority.WARNING -> AppTheme.LogColor.warning
            SimpleLogger.LogPriority.ERROR -> AppTheme.LogColor.error
            SimpleLogger.LogPriority.DEBUG -> AppTheme.LogColor.debug
        }

    private fun printLog(message: String?, priority: SimpleLogger.LogPriority) {
        if (message != null) {
            val annotatedLog =
                AnnotatedString(
                    "$currentTimeFormatted ${priority.simpleName}/$identity: $message",
                    SpanStyle(priority.color)
                )

            LoggerStorage.add(annotatedLog)
            writeToFile(message)
        }
    }

    public override fun verbose0(message: String?): Unit = printLog(message, SimpleLogger.LogPriority.VERBOSE)

    public override fun verbose0(message: String?, e: Throwable?) {
        if (e != null) verbose((message ?: e.toString()) + "\n${e.stackTraceString}")
        else verbose(message.toString())
    }

    public override fun info0(message: String?): Unit = printLog(message, SimpleLogger.LogPriority.INFO)
    public override fun info0(message: String?, e: Throwable?) {
        if (e != null) info((message ?: e.toString()) + "\n${e.stackTraceString}")
        else info(message.toString())
    }

    public override fun warning0(message: String?): Unit = printLog(message, SimpleLogger.LogPriority.WARNING)
    public override fun warning0(message: String?, e: Throwable?) {
        if (e != null) warning((message ?: e.toString()) + "\n${e.stackTraceString}")
        else warning(message.toString())
    }

    public override fun error0(message: String?): Unit = printLog(message, SimpleLogger.LogPriority.ERROR)
    public override fun error0(message: String?, e: Throwable?) {
        if (e != null) error((message ?: e.toString()) + "\n${e.stackTraceString}")
        else error(message.toString())
    }

    public override fun debug0(message: String?): Unit = printLog(message, SimpleLogger.LogPriority.DEBUG)
    public override fun debug0(message: String?, e: Throwable?) {
        if (e != null) debug((message ?: e.toString()) + "\n${e.stackTraceString}")
        else debug(message.toString())
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

package com.youngerhousea.mirai.compose.console.impl

import androidx.compose.ui.graphics.Color
import net.mamoe.mirai.utils.MiraiLoggerPlatformBase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

enum class TextColor(private val format: String) {
    RESET("\u001b[0m"),

    WHITE("\u001b[30m"),
    RED("\u001b[31m"),
    EMERALD_GREEN("\u001b[32m"),
    GOLD("\u001b[33m"),
    BLUE("\u001b[34m"),
    PURPLE("\u001b[35m"),
    GREEN("\u001b[36m"),

    GRAY("\u001b[90m"),
    LIGHT_RED("\u001b[91m"),
    LIGHT_GREEN("\u001b[92m"),
    LIGHT_YELLOW("\u001b[93m"),
    LIGHT_BLUE("\u001b[94m"),
    LIGHT_PURPLE("\u001b[95m"),
    LIGHT_CYAN("\u001b[96m");

    override fun toString(): String = format
}

enum class LogKind(
    val simpleName: String,
    val color: Color,
    val textColor: TextColor
) {
    VERBOSE("V", Color.Blue , TextColor.BLUE),
    INFO("I", Color.Green, TextColor.GREEN),
    WARNING("W", Color.Red, TextColor.RED),
    ERROR("E", Color.Red, TextColor.RED),
    DEBUG("D", Color.Cyan, TextColor.LIGHT_CYAN)
}

data class Log(
    val message: String,
    val kind: LogKind
)

private val timeFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE)

val currentTimeFormatted: String get() = timeFormat.format(Date())

class MiraiComposeLogger(
    override val identity: String?,
    val output: (content: String?, exception: Throwable?, priority: LogKind) -> Unit
) : MiraiLoggerPlatformBase() {

    public override fun verbose0(message: String?, e: Throwable?) =
        output(message, e, LogKind.VERBOSE)

    public override fun info0(message: String?, e: Throwable?) =
        output(message, e, LogKind.INFO)

    public override fun warning0(message: String?, e: Throwable?) =
        output(message, e, LogKind.WARNING)

    public override fun error0(message: String?, e: Throwable?) =
        output(message, e, LogKind.ERROR)

    public override fun debug0(message: String?, e: Throwable?) =
        output(message, e, LogKind.DEBUG)

}

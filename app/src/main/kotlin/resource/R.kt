package com.youngerhousea.mirai.compose.resource

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.loadXmlImageVector
import androidx.compose.ui.unit.Density
import org.xml.sax.InputSource
import java.io.File

object R {
    object Version {
        const val Frontend = "https://github.com/sonder-joker/mirai-compose"
        const val Backend = "https://github.com/mamoe/mirai"
    }

    object String {
        const val BotMenuExit = ""
        const val BotMenuAdd = ""
        const val RailTabFirst = "Message"
        const val RailTabSecond = "Plugin"
        const val RailTabThird = "Setting"
        const val RailTabFourth = "About"
        const val EditSuccess = "Success"
        const val EditFailure = "Failure"

        object Plugin {
            const val Add = "添加插件"
        }

        const val PasswordError = "密码错误"
        const val RetryLater = "请稍后再试"
        const val NotSupportSlider = "目前不支持滑动输入框"
        const val SMSLoginError = "Mirai暂未提供短信输入"
        const val NoStandInput = "无标准输入"
        const val NoServerError = "无可用服务器"
        const val IllPassword = "密码长度最多为16"
        const val UnknownError = "Unknown Reason"
        const val CancelLogin = ""
    }

    object Image {
        val Java = loadXmlImageVector(File("ic_java.xml"))
        val Kotlin = loadXmlImageVector(File("ic_kotlin.xml"))
        val Mirai = loadXmlImageVector(File("ic_mirai.xml"))
    }

    object Icon {
        val Message = Icons.Outlined.Message
        val Plugins = Icons.Outlined.Extension
        val Setting = Icons.Outlined.Settings
        val About = Icons.Outlined.Forum
        val Add = Icons.Filled.Add
        val Back = Icons.Default.KeyboardArrowLeft
        val Mirai = loadImageBitmap(File("mirai.png"))
    }
}

fun loadImageBitmap(file: File): ImageBitmap =
    file.inputStream().buffered().use(::loadImageBitmap)

fun loadSvgPainter(file: File, density: Density): Painter =
    file.inputStream().buffered().use { loadSvgPainter(it, density) }

fun loadXmlImageVector(file: File, density: Density = Density(1f)): ImageVector =
    file.inputStream().buffered().use { loadXmlImageVector(InputSource(it), density) }
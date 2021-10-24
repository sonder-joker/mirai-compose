package com.youngerhousea.mirai.compose.resource

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.loadXmlImageVector
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.Density
import org.xml.sax.InputSource

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
        const val RailTabFive = "ConsoleLog"
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

        const val Login = "Login"
        const val Password = "Password"
    }

    object Image {
        val Java = loadXmlImageVector("ic_java.xml")
        val Kotlin = loadXmlImageVector("ic_kotlin.xml")
        val Mirai = loadXmlImageVector("ic_mirai.xml")
    }

    object Icon {
        val Search = Icons.Default.Search
        val ConsoleLog = Icons.Outlined.Description
        val Message = Icons.Outlined.Message
        val Plugins = Icons.Outlined.Extension
        val Setting = Icons.Outlined.Settings
        val About = Icons.Outlined.Forum
        val Add = Icons.Filled.Add
        val Back = Icons.Default.KeyboardArrowLeft
        val Mirai = loadImageBitmap("mirai.png")
    }

    object Colors {
        val SearchText = Color.Yellow
        val TopAppBar = Color(0xFF69e2ff)
        val SplitLeft = Color(0xFF00b0ff)
    }
}

fun loadImageBitmap(path: String): ImageBitmap =
    useResource(path) { it.buffered().use(::loadImageBitmap) }

fun loadSvgPainter(path: String, density: Density = Density(1f)): Painter =
    useResource(path) { loadSvgPainter(it, density) }

fun loadXmlImageVector(path: String, density: Density = Density(1f)): ImageVector =
    useResource(path) { stream -> stream.buffered().use { loadXmlImageVector(InputSource(it), density) } }
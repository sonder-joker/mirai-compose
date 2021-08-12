package com.youngerhousea.mirai.compose.resource

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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

        object Plugin{
            const val Add = "添加插件"
        }
    }

    object Image {
        const val mirai = "ic_mirai.xml"
        const val java = "ic_java.xml"
        const val kotlin = "ic_kotlin.xml"
    }

    object Icon {
        val Message = Icons.Outlined.Message
        val Plugins = Icons.Outlined.Extension
        val Setting = Icons.Outlined.Settings
        val About = Icons.Outlined.Forum
        val Add = Icons.Filled.Add
    }
}

fun loadImageBitmap(file: File): ImageBitmap =
    file.inputStream().buffered().use(::loadImageBitmap)

fun loadSvgPainter(file: File, density: Density): Painter =
    file.inputStream().buffered().use { loadSvgPainter(it, density) }

fun loadXmlImageVector(file: File, density: Density): ImageVector =
    file.inputStream().buffered().use { loadXmlImageVector(InputSource(it), density) }
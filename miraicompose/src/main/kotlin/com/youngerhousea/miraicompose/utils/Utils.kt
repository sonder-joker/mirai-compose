package com.youngerhousea.miraicompose.utils

import androidx.compose.desktop.AppFrame
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.IntSize
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*

private val client = HttpClient()

suspend fun String.toAvatarImage(): ImageBitmap {
    return org.jetbrains.skija.Image.makeFromEncoded(
        client.get(this@toAvatarImage) {
            header("Connection", "close")
        }
    ).asImageBitmap()
}

fun AppFrame.setSize(size: IntSize) =
    this.setSize(size.width, size.height)

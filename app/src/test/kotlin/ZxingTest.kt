package com.youngerhousea.miraicompose.app

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    val a =
        Window {
            QRCode("测试")
        }
}

@Composable
fun QRCode(string: String) {
    val a = QRCodeWriter().encode(string, BarcodeFormat.QR_CODE, 200, 200)

    Canvas(Modifier) {
        for (y in 0 until a.height) {
            for (x in 0 until a.width) {
                drawRect(
                    if (a.get(x, y)) Color.White else Color.Black,
                    topLeft = Offset(x.toFloat(), y.toFloat()),
                    size = Size(1f, 1f)
                )
            }
        }
    }
}



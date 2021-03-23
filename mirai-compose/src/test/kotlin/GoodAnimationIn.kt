package com.youngerhousea.miraicompose

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.Window
import androidx.compose.desktop.WindowEvents
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.model.ComposeBot
import kotlinx.coroutines.delay


fun main() = Window(
    undecorated = true,
    size = IntSize(400, 400),
) {
    var a by remember { mutableStateOf(50f) }
    val b by animateFloatAsState(a)

    LaunchedEffect(Unit) {
        while (true) {
            delay(10)
            a++
            if (a > 200f) {
                a = 0f
            }
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xffe8e0cb))
    ) {
        drawCircle(Color.Red, radius = b, style = Stroke(1.5f))
        drawCircle(Color.Red, radius = 100f, style = Stroke(1.5f))
        drawCircle(Color.Red, radius = 150f, style = Stroke(1.5f))

    }

}
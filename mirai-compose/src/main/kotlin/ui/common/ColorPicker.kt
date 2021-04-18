package com.youngerhousea.miraicompose.ui.common

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp

@Composable
fun ColorPicker(
    initialColor: Color,
    firstCanvasWidth: Int = 20,
    firstCanvasHeight: Int = 1,
    secondCanvasSingleElementSize: Int = 1,
    result: (red: Int, green: Int, blue: Int, alpha: Float) -> Unit
) {
    var red by remember { mutableStateOf(0) }
    var green by remember { mutableStateOf(0) }
    var blue by remember { mutableStateOf(0) }

    var trueRed by remember { mutableStateOf(initialColor.red.toInt()) }
    var trueGreen by remember { mutableStateOf(initialColor.green.toInt()) }
    var trueBlue by remember { mutableStateOf(initialColor.blue.toInt()) }
    var trueAlpha by remember { mutableStateOf(initialColor.alpha) }

    Row(Modifier.animateContentSize()) {
        Canvas(modifier = Modifier.pointerMoveFilter(onMove = { position ->
            red = position.y.toInt() / firstCanvasHeight
            true
        }).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
            trueRed = red
            result(trueRed, trueGreen, trueBlue, trueAlpha)
        }.size(firstCanvasWidth.dp, (256 * firstCanvasHeight).dp)) {
            for (x in 0..255) {
                drawBackground(0, x, Color(x, 0, 0), firstCanvasWidth, firstCanvasHeight)
            }
        }

        Canvas(modifier = Modifier.pointerMoveFilter(onMove = { position ->
            blue = position.x.toInt() / secondCanvasSingleElementSize
            green = position.y.toInt() / secondCanvasSingleElementSize
            true
        }).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
            trueGreen = green
            trueBlue = blue
            result(trueRed, trueGreen, trueBlue, trueAlpha)
        }.size((secondCanvasSingleElementSize * 256).dp)) {
            for (x in 0..255) {
                for (y in 0..255) {
                    drawBackground(
                        x,
                        y,
                        Color(red, y, x),
                        secondCanvasSingleElementSize,
                        secondCanvasSingleElementSize
                    )
                }
            }
        }
        Slider(trueAlpha, {
            trueAlpha = it
        }, Modifier.width(100.dp))
        Text("x :$trueRed y: $trueGreen z: $trueBlue")
        Text("currentX :$red currentY: $green currentZ: $blue")
    }
}


private fun DrawScope.drawBackground(x: Int, y: Int, color: Color, widthRate: Int, heightRate: Int) {
    drawRect(
        color = color,
        topLeft = Offset(x = x * widthRate.toFloat(), y = y * heightRate.toFloat()),
        size = Size(widthRate.toFloat(), heightRate.toFloat())
    )
}
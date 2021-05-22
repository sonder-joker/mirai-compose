package com.youngerhousea.miraicompose.ui.common

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.utils.*

@Composable
fun ColorPicker(
    initialColor: Color,
    firstCanvasWidth: Int = 20,
    firstCanvasHeight: Int = 1,
    secondCanvasSingleElementSize: Int = 1,
    result: (red: Int, green: Int, blue: Int, alpha: Int) -> Unit
) {
    var red by remember { mutableStateOf(initialColor.r) }
    var green by remember { mutableStateOf(initialColor.g) }
    var blue by remember { mutableStateOf(initialColor.b) }

    var trueRed by remember { mutableStateOf(initialColor.r) }
    var trueGreen by remember { mutableStateOf(initialColor.g) }
    var trueBlue by remember { mutableStateOf(initialColor.b) }
    var trueAlpha by remember { mutableStateOf(initialColor.a) }

    Row(Modifier.fillMaxSize().animateContentSize(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Canvas(modifier = Modifier.pointerMoveFilter(onMove = { position ->
            red = position.y.toInt() / firstCanvasHeight
            true
        }).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
            trueRed = red
            result(trueRed, trueGreen, trueBlue, trueAlpha)
        }.size(firstCanvasWidth.dp, (256 * firstCanvasHeight).dp)) {
            for (x in 0..255) {
                drawBackground(0, x, Color(x, 0, 0, trueAlpha), firstCanvasWidth, firstCanvasHeight)
            }
        }
        Canvas(modifier = Modifier.pointerMoveFilter(onMove = { position ->
            blue = position.x.toInt() / secondCanvasSingleElementSize
            green = position.y.toInt() / secondCanvasSingleElementSize
            true
        }).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
            trueRed = red
            trueGreen = green
            trueBlue = blue
            result(trueRed, trueGreen, trueBlue, trueAlpha)
        }.size((secondCanvasSingleElementSize * 256).dp)) {
            for (x in 0..255) {
                for (y in 0..255) {
                    drawBackground(
                        x,
                        y,
                        Color(red, y, x, trueAlpha),
                        secondCanvasSingleElementSize,
                        secondCanvasSingleElementSize
                    )
                }
            }
        }
        IntSlider(trueAlpha, {
            trueAlpha = it
            result(trueRed, trueGreen, trueBlue, trueAlpha)
        }, valueRange = 0..0xff, modifier = Modifier.width(100.dp))
        Row {
            Text("Pointer color")
            Image(ColorPainter(Color(red, green, blue, trueAlpha)), null, Modifier.width(20.dp).height(20.dp))
        }
        Row {
            Text("Now color")
            Image(
                ColorPainter(Color(trueRed, trueGreen, trueBlue, trueAlpha)),
                null,
                Modifier.width(20.dp).height(20.dp)
            )
        }

    }
}


private fun DrawScope.drawBackground(x: Int, y: Int, color: Color, widthRate: Int, heightRate: Int) {
    drawRect(
        color = color,
        topLeft = Offset(x = x * widthRate.toFloat(), y = y * heightRate.toFloat()),
        size = Size(widthRate.toFloat(), heightRate.toFloat())
    )
}
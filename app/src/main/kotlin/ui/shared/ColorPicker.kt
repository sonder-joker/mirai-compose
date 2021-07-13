package com.youngerhousea.miraicompose.app.ui.shared

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
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

//need more optimization
@Composable
fun ColorPicker(
    color: Color,
    firstCanvasWidth: Float = 20f,
    firstCanvasHeight: Float = 1f,
    secondCanvasSingleElementSize: Float = 1f,
    onColorChange: (color: Color) -> Unit
) {
    // pointer move rgb
    var red by remember { mutableStateOf(color.red) }
    var green by remember { mutableStateOf(color.green) }
    var blue by remember { mutableStateOf(color.blue) }
    var alpha by remember { mutableStateOf(color.alpha) }

    Row(Modifier.fillMaxSize().animateContentSize(), horizontalArrangement = Arrangement.SpaceEvenly) {

        Canvas(modifier = Modifier.pointerMoveFilter(onMove = { position ->
            red = (position.y / firstCanvasHeight) / 255f
            true
        }).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
            Color(red, green, blue, alpha)
        }.size(firstCanvasWidth.dp, (256 * firstCanvasHeight).dp)) {
            for (x in 0..255) {
                drawBackground(0f, x.toFloat(), Color(x / 255f, 0f, 0f, alpha), firstCanvasWidth, firstCanvasHeight)
            }
        }

        Canvas(modifier = Modifier.pointerMoveFilter(onMove = { position ->
            blue = position.x / secondCanvasSingleElementSize / 255f
            green = position.y / secondCanvasSingleElementSize / 255f
            true
        }).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
            onColorChange(Color(red, green, blue, alpha))
        }.size((secondCanvasSingleElementSize * 256).dp)) {
            for (x in 0..255) {
                for (y in 0..255) {
                    drawBackground(
                        x / 255f,
                        y / 255f,
                        Color(red, y / 255f, x / 255f, alpha),
                        secondCanvasSingleElementSize,
                        secondCanvasSingleElementSize
                    )
                }
            }
        }

        Slider(alpha, {
            alpha = it
            onColorChange(Color(red, green, blue, alpha))
        }, valueRange = 0f..1f, modifier = Modifier.width(100.dp))

        Row {
            Text("Pointer color")
            Image(ColorPainter(Color(red, green, blue, alpha)), null, Modifier.width(20.dp).height(20.dp))
        }

        Row {
            Text("Now color")
            Image(
                ColorPainter(color),
                null,
                Modifier.width(20.dp).height(20.dp)
            )
        }

    }
}


private fun DrawScope.drawBackground(x: Float, y: Float, color: Color, widthRate: Float, heightRate: Float) {
    drawRect(
        color = color,
        topLeft = Offset(x = x * widthRate, y = y * heightRate),
        size = Size(widthRate, heightRate)
    )
}

@Composable
@Suppress("DuplicatedCode")
fun ColorPicker(
    color: Color,
    onColorChange: (color: Color) -> Unit
) {
    Column {
        Slider(color.alpha, {
            onColorChange(color.copy(alpha = it))
        }, valueRange = 0f..1f)

        Slider(color.red, {
            onColorChange(color.copy(red = it))
        }, valueRange = 0f..1f)

        Slider(color.green, {
            onColorChange(color.copy(green = it))
        }, valueRange = 0f..1f)

        Slider(color.blue, {
            onColorChange(color.copy(blue = it))
        }, valueRange = 0f..1f)
    }
}

import androidx.compose.desktop.Window
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

fun main() = Window(title = "Compose for Desktop") {
    var x by remember { mutableStateOf(0) }
    var y by remember { mutableStateOf(0) }
    var z by remember { mutableStateOf(0) }

    val firstCanvasWidth = 20
    val firstCanvasHeight = 1

    val spaceWidth = 10
    val firstHeight = 256 * firstCanvasWidth

    val secondCanvasSingleElementSize = 1
    val secondSize = secondCanvasSingleElementSize * 256
    Row {
        Canvas(modifier = Modifier.pointerInput(Unit) {
            forEachGesture {
                awaitPointerEventScope {
                    awaitEventFirstDown().also { pointerEvent ->
                        pointerEvent.changes.forEach { it.consumeDownChange() }
                    }.mouseEvent?.let {
                        println("pointer now :${it.paramString()}")
                        x = it.y / firstCanvasHeight
                    }
                }
            }
        }.width(firstCanvasWidth.dp).height(firstHeight.dp)) {
            for (xx in 0..255) {
                drawBackground(0, xx, Color(xx, 0, 0), firstCanvasWidth, firstCanvasHeight)
            }
        }
        Spacer(Modifier.width(spaceWidth.dp).fillMaxHeight())
        Canvas(modifier = Modifier.pointerInput(Unit) {
            forEachGesture {
                awaitPointerEventScope {
                    awaitEventFirstDown().also { pointerEvent ->
                        pointerEvent.changes.forEach { it.consumeDownChange() }
                    }.mouseEvent?.let {
                        println("pointer now :${it.paramString()}")
                        z = (it.x - firstCanvasWidth - spaceWidth) / secondCanvasSingleElementSize
                        y = (it.y) / secondCanvasSingleElementSize
                    }
                }
            }
        }.size(secondSize.dp)) {
            for (xx in 0..255) {
                for (yy in 0..255) {
                    drawBackground(
                        xx,
                        yy,
                        Color(x, yy, xx),
                        secondCanvasSingleElementSize,
                        secondCanvasSingleElementSize
                    )
                }
            }
        }
    }

}

private suspend fun AwaitPointerEventScope.awaitEventFirstDown(): PointerEvent {
    var event: PointerEvent
    do {
        event = awaitPointerEvent()
    } while (
        !event.changes.all { it.changedToDown() }
    )
    return event
}

private fun DrawScope.drawBackground(x: Int, y: Int, color: Color, widthRate: Int, heightRate: Int) {
    drawRect(
        color = color,
        topLeft = Offset(x = x * widthRate.toFloat(), y = y * heightRate.toFloat()),
        size = Size(widthRate.toFloat(), heightRate.toFloat())
    )
}
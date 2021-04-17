import androidx.compose.desktop.Window
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.delay
import java.awt.event.MouseEvent

fun main() = Window(title = "Compose for Desktop") {
    var red by remember { mutableStateOf(0) }
    var green by remember { mutableStateOf(0) }
    var blue by remember { mutableStateOf(0) }

    val firstCanvasWidth = 20
    val firstCanvasHeight = 1

    val spaceWidth = 10
    val firstHeight = 256 * firstCanvasWidth

    val secondCanvasSingleElementSize = 1
    val secondSize = secondCanvasSingleElementSize * 256
    var isExpand by remember { mutableStateOf(false) }


    Row {
        Canvas(modifier = Modifier.onMouseDown(Unit) {
            red = it.y / firstCanvasHeight
        }.size(firstCanvasWidth.dp, firstHeight.dp)) {
            for (xx in 0..255) {
                drawBackground(0, xx, Color(xx, 0, 0), firstCanvasWidth, firstCanvasHeight)
            }
        }
        Spacer(Modifier.width(spaceWidth.dp).fillMaxHeight())
        Canvas(modifier = Modifier.onMouseDown(Unit) {
            blue = (it.x - firstCanvasWidth - spaceWidth) / secondCanvasSingleElementSize
            green = (it.y) / secondCanvasSingleElementSize
        }.size(secondSize.dp)) {
            for (xx in 0..255) {
                for (yy in 0..255) {
                    drawBackground(
                        xx,
                        yy,
                        Color(red, yy, xx),
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

fun Modifier.onMouseDown(key1: Any?, block: (MouseEvent) -> Unit) = pointerInput(key1) {
    forEachGesture {
        awaitPointerEventScope {
            awaitEventFirstDown().also { pointerEvent ->
                pointerEvent.changes.forEach { it.consumeDownChange() }
            }.mouseEvent?.apply(block)
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

fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = Modifier.layout { measurable, constraints ->
    // Measure the composable
    val placeable = measurable.measure(constraints)

    // Check the composable has a first baseline
    check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
    val firstBaseline = placeable[FirstBaseline]

    // Height of the composable with padding - first baseline
    val placeableY = firstBaselineToTop.toPx().toInt() - firstBaseline
    val height = placeable.height + placeableY
    layout(placeable.width, height) {
        // Where the composable gets placed
        placeable.place(0, placeableY)
    }
}
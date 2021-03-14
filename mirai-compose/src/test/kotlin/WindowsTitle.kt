package com.youngerhousea.miraicompose

import androidx.compose.desktop.AppManager
import androidx.compose.desktop.LocalAppWindow
import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Expand
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.ui.common.cursorForHorizontalResize
import java.awt.Cursor
import javax.swing.JFrame

fun main() = Window(undecorated = true) {
    var x by mutableStateOf(0)
    var y by mutableStateOf(0)

    val splitterState = SplitterState()
    val height by mutableStateOf(0)

    val initialSize by mutableStateOf(0)
    Column() {
        TopAppBar(
            Modifier
                .fillMaxSize()
                .draggable(
                state = DraggableState {/* initialSize += */},
                orientation = Orientation.Horizontal,
                startDragImmediately = true,
                onDragStarted = { splitterState.isResizing = true },
                onDragStopped = { splitterState.isResizing = false },
            ).cursorForHorizontalResize()
        ) {
            val app = AppManager.windows[0]
            Text("ceshi")
            Icon(Icons.Default.Close, null, Modifier.clickable {
                app.window.extendedState = JFrame.ICONIFIED
            })
            Icon(Icons.Default.Expand, null, Modifier.clickable {
                if (app.isFullscreen) {
                    app.setLocation(x, y)
                } else {
                    x = app.x
                    y = app.y
                    app.makeFullscreen()
                }
            })
            Icon(Icons.Default.Close, null, Modifier.clickable {
                //may be delete, give option
                app.window.extendedState = JFrame.ICONIFIED
            })
        }
        Text("?")
    }
}

internal fun Modifier.cursorForHorizontalResize(): Modifier = composed {
    var isHover by remember { mutableStateOf(false) }

    if (isHover) {
        LocalAppWindow.current.window.cursor = Cursor(Cursor.E_RESIZE_CURSOR)
    } else {
        LocalAppWindow.current.window.cursor = Cursor.getDefaultCursor()
    }

    pointerMoveFilter(
        onEnter = { isHover = true; true },
        onExit = { isHover = false; true }
    )
}

class SplitterState {
    var isResizing by mutableStateOf(false)
    var isResizeEnabled by mutableStateOf(true)
}

//private suspend fun AwaitPointerEventScope.awaitEventFirstDown(): PointerEvent {
//    var event: PointerEvent
//    do {
//        event = awaitPointerEvent()
//    } while (
//        !event.changes.all { it.changedToDown() }
//    )
//    return event
//}


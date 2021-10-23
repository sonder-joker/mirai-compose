package com.youngerhousea.mirai.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Maximize
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.youngerhousea.mirai.compose.resource.R
import java.awt.MouseInfo
import java.awt.Point
import java.awt.Window
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter

@Composable
fun MiraiComposeWindow(
    onCloseRequest: () -> Unit,
    state: WindowState = rememberWindowState(size = MiraiComposeWindowSize),
    visible: Boolean = true,
    enabled: Boolean = true,
    focusable: Boolean = true,
    alwaysOnTop: Boolean = false,
    onPreviewKeyEvent: (KeyEvent) -> Boolean = { false },
    onKeyEvent: (KeyEvent) -> Boolean = { false },
    content: @Composable WindowScope.() -> Unit,
) = Window(
    onCloseRequest = onCloseRequest,
    state = state,
    visible = visible,
    icon = null,
    undecorated = true,
    resizable = true,
    enabled = enabled,
    focusable = focusable,
    alwaysOnTop = alwaysOnTop,
    onPreviewKeyEvent = onPreviewKeyEvent,
    onKeyEvent = onKeyEvent
) {
    WindowArea(
        minimizeButton = {
            Button(
                onClick = {
                    state.isMinimized = true
                }
            ) {
                Icon(Icons.Default.Minimize, null)
            }
        },
        maximizeButton = {
            Button(
                onClick = {
                    state.placement = when (state.placement) {
                        WindowPlacement.Floating -> WindowPlacement.Maximized
                        WindowPlacement.Maximized -> WindowPlacement.Floating
                        WindowPlacement.Fullscreen -> WindowPlacement.Floating
                    }
                }
            ) {
                Icon(Icons.Default.Maximize, null)
            }
        },
        exitButton = {
            Button(
                onClick = onCloseRequest
            ) {
                Icon(Icons.Default.Close, null)
            }
        },
        draggableAreaHeight = WindowDraggableHeight,
    ) {
        content()
    }
}


@Composable
fun MiraiComposeDialog(
    onCloseRequest: () -> Unit,
    state: DialogState = rememberDialogState(),
    visible: Boolean = true,
    title: String = "Untitled",
    icon: Painter? = null,
    enabled: Boolean = true,
    focusable: Boolean = true,
    onPreviewKeyEvent: ((KeyEvent) -> Boolean) = { false },
    onKeyEvent: ((KeyEvent) -> Boolean) = { false },
    content: @Composable DialogWindowScope.() -> Unit
) = Dialog(
    onCloseRequest = onCloseRequest,
    state = state,
    visible = visible,
    title = title,
    icon = icon,
    undecorated = true,
    resizable = true,
    enabled = enabled,
    focusable = focusable,
    onPreviewKeyEvent = onPreviewKeyEvent,
    onKeyEvent = onKeyEvent
) {
    WindowArea(
        exitButton = {
            Button(
                onClick = onCloseRequest
            ) {
                Icon(Icons.Default.Close, null)
            }
        },
        draggableAreaHeight = DialogDraggableHeight
    ) {
        content()
    }
}


@Composable
private inline fun WindowScope.WindowArea(
    crossinline minimizeButton: @Composable () -> Unit = {},
    crossinline maximizeButton: @Composable () -> Unit = {},
    crossinline exitButton: @Composable () -> Unit = {},
    draggableAreaHeight: Dp = DialogDraggableHeight,
    crossinline content: @Composable () -> Unit
) {
    Surface(elevation = 1.dp) {
        Column(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.background(color = R.Colors.TopAppBar)
                    .fillMaxWidth()
                    .height(draggableAreaHeight)
                    .padding(start = DraggableLeftStart, end = DraggableRightStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                WindowDraggableArea(
                    modifier = Modifier.weight(1f)
                ) {

                }
                Row {
                    minimizeButton()
                    Spacer(modifier = Modifier.width(DraggableIconSpace))
                    maximizeButton()
                    Spacer(modifier = Modifier.width(DraggableIconSpace))
                    exitButton()
                }
            }
            content()
        }
    }
}

@Preview
@Composable
fun WindowsAreaPreview() {
    Window({}) {
        WindowArea {
            Text("Test")
        }
    }
}

private val MiraiComposeWindowSize = DpSize(1280.dp, 768.dp)

private val DraggableLeftStart = 20.dp
private val DraggableRightStart = 10.dp
private val WindowDraggableHeight = 30.dp
private val DialogDraggableHeight = 20.dp
private val DraggableIconSpace = 5.dp

// remove on future
@Composable
fun WindowScope.WindowDraggableArea(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    val handler = remember { DragHandler(window) }

    Box(
        modifier = modifier.fillMaxSize().pointerInput(Unit) {
            forEachGesture {
                awaitPointerEventScope {
                    awaitFirstDown()
                    handler.register()
                }
            }
        }
    ) {
        content()
    }
}

private class DragHandler(private val window: Window) {
    private var location = window.location.toComposeOffset()
    private var pointStart = MouseInfo.getPointerInfo().location.toComposeOffset()

    private val dragListener = object : MouseMotionAdapter() {
        override fun mouseDragged(event: MouseEvent) = drag()
    }
    private val removeListener = object : MouseAdapter() {
        override fun mouseReleased(event: MouseEvent) {
            window.removeMouseMotionListener(dragListener)
            window.removeMouseListener(this)
        }
    }

    fun register() {
        location = window.location.toComposeOffset()
        pointStart = MouseInfo.getPointerInfo().location.toComposeOffset()
        window.addMouseListener(removeListener)
        window.addMouseMotionListener(dragListener)
    }

    private fun drag() {
        val point = MouseInfo.getPointerInfo().location.toComposeOffset()
        val location = location + (point - pointStart)
        window.setLocation(location.x, location.y)
    }

    private fun Point.toComposeOffset() = IntOffset(x, y)
}
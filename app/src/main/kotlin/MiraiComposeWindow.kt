package com.youngerhousea.mirai.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Maximize
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*

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
    WindowsArea(
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
    WindowsArea(
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
private inline fun WindowScope.WindowsArea(
    crossinline minimizeButton: @Composable () -> Unit = {},
    crossinline maximizeButton: @Composable () -> Unit = {},
    crossinline exitButton: @Composable () -> Unit = {},
    draggableAreaHeight: Dp = DialogDraggableHeight,
    content: @Composable () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.background(color = Color(75, 75, 75))
                .fillMaxWidth()
                .height(draggableAreaHeight)
                .padding(start = DraggableLeftStart, end = DraggableRightStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WindowDraggableArea(
                modifier = Modifier.weight(1f)
            ) {
                // Don't know why if empty can't drag windows
                Text(text = "", color = Color.White)
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


@Preview
@Composable
fun WindowsAreaPreview() {
    Window({}) {
        WindowsArea {
            Text("Test")
        }
    }
}

private val MiraiComposeWindowSize = WindowSize(1280.dp, 768.dp)

private val DraggableLeftStart = 20.dp
private val DraggableRightStart = 10.dp
private val WindowDraggableHeight = 30.dp
private val DialogDraggableHeight = 10.dp
private val DraggableIconSpace = 5.dp
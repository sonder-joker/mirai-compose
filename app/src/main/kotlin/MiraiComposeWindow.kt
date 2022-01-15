package com.youngerhousea.mirai.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloseFullscreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.youngerhousea.mirai.compose.resource.R
import com.youngerhousea.mirai.compose.ui.closeIcon
import com.youngerhousea.mirai.compose.ui.maxIcon
import com.youngerhousea.mirai.compose.ui.minIcon

@Composable
fun MiraiComposeWindow(
    onLoaded: () -> Unit,
    onCloseRequest: () -> Unit,
    state: WindowState = rememberWindowState(size = MiraiComposeWindowSize),
    content: @Composable WindowScope.() -> Unit,
) = Window(
    onLoaded = onLoaded,
    onCloseRequest = onCloseRequest,
    state = state,
    title = "MiraiCompose",
    icon = BitmapPainter(R.Icon.Mirai),
    undecorated = true,
    resizable = true,
) {
    WindowArea(
        minimizeButton = {
            Button(
                onClick = {
                    state.isMinimized = true
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = R.Colors.TopAppBar)
            ) {
                Icon(minIcon(), null)
            }
        },
        maximizeButton = {
            Button(
                onClick = state::onMaximizeButtonClick,
                colors = ButtonDefaults.buttonColors(backgroundColor = R.Colors.TopAppBar)
            ) {
                when (state.placement) {
                    WindowPlacement.Maximized,
                    WindowPlacement.Fullscreen -> Icon(Icons.Default.CloseFullscreen, null)
                    WindowPlacement.Floating -> Icon(maxIcon(), null)
                }

            }
        },
        exitButton = {
            Button(
                onClick = onCloseRequest,
                colors = ButtonDefaults.buttonColors(backgroundColor = R.Colors.TopAppBar)
            ) {
                Icon(closeIcon(12f), null)
            }
        },
        onBarDoubleClick = state::onMaximizeButtonClick,
        draggableAreaHeight = WindowDraggableHeight,
    ) {
        content()
    }
}

fun WindowState.onMaximizeButtonClick() {
    placement = when (placement) {
        WindowPlacement.Floating -> WindowPlacement.Maximized
        WindowPlacement.Maximized -> WindowPlacement.Floating
        WindowPlacement.Fullscreen -> WindowPlacement.Floating
    }
}


@Composable
fun MiraiComposeDialog(
    onCloseRequest: () -> Unit,
    state: DialogState = rememberDialogState(size = DpSize(642.dp, 376.dp)),
    visible: Boolean = true,
    title: String = "Dialog",
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
                onClick = onCloseRequest,
                colors = ButtonDefaults.buttonColors(backgroundColor = R.Colors.TopAppBar)
            ) {
                Icon(closeIcon(50f), null)
            }
        },
    ) {
        content()
    }
}


@Composable
private inline fun WindowScope.WindowArea(
    crossinline minimizeButton: @Composable () -> Unit = {},
    crossinline maximizeButton: @Composable () -> Unit = {},
    crossinline exitButton: @Composable () -> Unit = {},
    noinline onBarDoubleClick: () -> Unit = {},
    draggableAreaHeight: Dp = DialogDraggableHeight,
    crossinline content: @Composable () -> Unit
) {
    Surface(elevation = 1.dp) {
        Column(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.background(color = R.Colors.TopAppBar)
                    .fillMaxWidth()
                    .height(draggableAreaHeight)
                    .padding(horizontal = DraggableRightStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                WindowDraggableArea(
                    modifier = Modifier.fillMaxSize().weight(1f)
                        .combinedClickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onDoubleClick = onBarDoubleClick,
                            onClick = {}
                        )
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

private val DraggableRightStart = 10.dp
private val WindowDraggableHeight = 30.dp
private val DialogDraggableHeight = 20.dp
private val DraggableIconSpace = 5.dp

package com.youngerhousea.miraicompose

import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.LocalAppWindow
import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowDraggableArea
import javax.swing.JFrame

fun main() {
    Window(
        undecorated = true
    ) {
        val current: AppWindow = LocalAppWindow.current
        Row(
            modifier = Modifier.background(color = Color(75, 75, 75))
                .fillMaxWidth()
                .height(30.dp)
                .padding(start = 20.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WindowDraggableArea(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Undecorated window", color = Color.White)
            }
            Row {
                Button(
                    onClick = {
                        current.window.extendedState = JFrame.ICONIFIED
                    }
                )
                Spacer(modifier = Modifier.width(5.dp))
                Button(
                    onClick = {
                        if (current.window.extendedState == JFrame.MAXIMIZED_BOTH) {
                            current.window.extendedState = JFrame.NORMAL
                        } else {
                            current.window.extendedState = JFrame.MAXIMIZED_BOTH
                        }
                    }
                )
                Spacer(modifier = Modifier.width(5.dp))
                Button(
                    onClick = {
                        current.window.parent.close()
                    }
                )
            }
        }
    }
}

@Composable
fun Button(
    text: String = "",
    onClick: () -> Unit = {},
    color: Color = Color(210, 210, 210),
    size: Int = 16
) {
    val buttonHover = remember { mutableStateOf(false) }
    Surface(
        color = if (buttonHover.value)
            Color(color.red / 1.3f, color.green / 1.3f, color.blue / 1.3f)
        else
            color,
        shape = RoundedCornerShape((size / 2).dp)
    ) {
        Box(
            modifier = Modifier
                .clickable(onClick = onClick)
                .size(size.dp, size.dp)
                .pointerMoveFilter(
                    onEnter = {
                        buttonHover.value = true
                        false
                    },
                    onExit = {
                        buttonHover.value = false
                        false
                    },
                    onMove = { false }
                )
        ) {
            Text(text = text)
        }
    }
}
package com.youngerhousea.mirai.compose.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window

@Composable
fun ExceptionWindow(
    onCloseRequest: () -> Unit,
    visible: Boolean,
    message: String
) {
    Window(onCloseRequest, visible = visible) {
        ExceptionMessage(message)
    }
}

@Composable
fun ExceptionMessage(message: String) {
    Column {
        Text(message)
    }
}

@Composable
@Preview
fun ExceptionMessagePreview() {
    ExceptionMessage("aaaaaaaaaaaa")
}
package com.youngerhousea.miraicompose.ui.plugin

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.*
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.youngerhousea.miraicompose.component.plugin.CJvmPlugin


@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun CJvmPluginUi(CJvmPlugin: CJvmPlugin) {
    Column {
        var index by remember { mutableStateOf(0) }
        TabRow(index) {
            Tab(
                selectedContentColor = Color.Black,
                text = { Text("Description") },
                selected = index == 0,
                onClick = {
                    CJvmPlugin.onDescriptionClick()
                    index = 0
                }
            )
            Tab(
                selectedContentColor = Color.Black,
                text = { Text("Data") },
                selected = index == 1,
                onClick = {
                    CJvmPlugin.onDataClick()
                    index = 1
                }
            )
            Tab(
                selectedContentColor = Color.Black,
                text = { Text("Command") },
                selected = index == 2,
                onClick = {
                    CJvmPlugin.onCommandClick()
                    index = 2
                }
            )
        }

        Children(CJvmPlugin.state, crossfade()) { child ->
            child.instance()
        }
    }
}

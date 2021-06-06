package com.youngerhousea.miraicompose.app.ui.plugin

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.*
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.youngerhousea.miraicompose.core.component.plugin.CKotlinPlugin
import com.youngerhousea.miraicompose.core.component.plugin.shared.DetailedCommand
import com.youngerhousea.miraicompose.core.component.plugin.shared.DetailedData
import com.youngerhousea.miraicompose.core.component.plugin.shared.DetailedDescription


@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun CKotlinPluginUi(CKotlinPlugin: CKotlinPlugin) {
    Column {
        var index by remember { mutableStateOf(0) }

        TabRow(index) {
            Tab(
                selectedContentColor = Color.Black,
                text = { Text("Description") },
                selected = index == 0,
                onClick = {
                    CKotlinPlugin.onDescriptionClick()
                    index = 0
                }
            )
            Tab(
                selectedContentColor = Color.Black,
                text = { Text("Data") },
                selected = index == 1,
                onClick = {
                    CKotlinPlugin.onDataClick()
                    index = 1
                }
            )
            Tab(
                selectedContentColor = Color.Black,
                text = { Text("Command") },
                selected = index == 2,
                onClick = {
                    CKotlinPlugin.onCommandClick()
                    index = 2
                }
            )
        }

        Children(CKotlinPlugin.state, crossfade()) { child ->
            when (val ch = child.instance) {
                is DetailedDescription -> DetailedDescriptionUi(ch)
                is DetailedCommand -> DetailedCommandUi(ch)
                is DetailedData -> DetailedDataUi(ch)
            }
        }
    }
}

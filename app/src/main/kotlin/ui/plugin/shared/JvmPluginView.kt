package com.youngerhousea.miraicompose.app.ui.plugin.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.arkivanov.decompose.value.Value
import com.youngerhousea.miraicompose.app.ui.plugin.DetailedCommandUi
import com.youngerhousea.miraicompose.app.ui.plugin.DetailedDataUi
import com.youngerhousea.miraicompose.app.ui.plugin.DetailedDescriptionUi
import com.youngerhousea.miraicompose.core.component.plugin.shared.DetailedCommand
import com.youngerhousea.miraicompose.core.component.plugin.shared.DetailedData
import com.youngerhousea.miraicompose.core.component.plugin.shared.DetailedDescription

@OptIn(ExperimentalDecomposeApi::class)
fun JvmPluginUi(
    state: Value<RouterState<*, ComponentContext>>,
    onDescriptionClick: () -> Unit,
    onDataClick:() -> Unit,
    onCommandClick:() -> Unit
) {
    Column {
        var index by remember { mutableStateOf(0) }
        TabRow(index) {
            Tab(
                selectedContentColor = Color.Black,
                text = { Text("Description") },
                selected = index == 0,
                onClick = {
                    onDescriptionClick()
                    index = 0
                }
            )
            Tab(
                selectedContentColor = Color.Black,
                text = { Text("Data") },
                selected = index == 1,
                onClick = {
                    onDataClick()
                    index = 1
                }
            )
            Tab(
                selectedContentColor = Color.Black,
                text = { Text("Command") },
                selected = index == 2,
                onClick = {
                    onCommandClick()
                    index = 2
                }
            )
        }

        Children(state, crossfade()) { child ->
            when (val ch = child.instance) {
                is DetailedDescription -> DetailedDescriptionUi(ch)
                is DetailedCommand -> DetailedCommandUi(ch)
                is DetailedData -> DetailedDataUi(ch)
            }
        }
    }
}
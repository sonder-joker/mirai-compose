package com.youngerhousea.miraicompose.ui.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Constraints

@Composable
fun ErrorAbleButton(onClick: () -> Unit, content: @Composable RowScope.() -> Unit) {
    var isError by remember { mutableStateOf("") }
    Button(
        {
            onClick()
        }, content = content
    )
}
package com.youngerhousea.miraicompose.utils

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext

typealias Component = @Composable () -> Unit

fun <T : ComponentContext> T.asComponent(content: @Composable (T) -> Unit): Component {
    return {
        content(this)
    }
}
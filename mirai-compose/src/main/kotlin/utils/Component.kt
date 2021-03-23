package com.youngerhousea.miraicompose.utils

import androidx.compose.runtime.Composable

typealias Component = @Composable () -> Unit

fun <T : Any> T.asComponent(content: @Composable (T) -> Unit): Component {
    return {
        content(this)
    }
}
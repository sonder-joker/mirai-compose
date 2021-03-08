package com.youngerhousea.miraicompose.utils

import androidx.compose.runtime.Composable


fun interface Component {
    @Composable
    fun render()
}
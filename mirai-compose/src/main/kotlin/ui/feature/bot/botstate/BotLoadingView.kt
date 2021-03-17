package com.youngerhousea.miraicompose.ui.feature.bot.botstate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.utils.Component

class BotLoading(context: ComponentContext) : Component, ComponentContext by context {
    @Composable
    override fun render() {
        BotLoadingView()
    }
}

@Composable
fun BotLoadingView() = Box(
    Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    CircularProgressIndicator()
}
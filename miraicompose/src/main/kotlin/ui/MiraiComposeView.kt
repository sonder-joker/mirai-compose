package com.youngerhousea.miraicompose.ui

import androidx.compose.desktop.Window
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.unit.IntSize
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.theme.AppTheme
import com.youngerhousea.miraicompose.theme.ResourceImage
import com.youngerhousea.miraicompose.ui.navigation.NavHost
import kotlinx.coroutines.cancel

fun MiraiComposeView() {
    Window(
        title = "Mirai Compose",
        size = IntSize(1280, 768),
        icon = ResourceImage.icon,
        onDismissRequest = {
            MiraiCompose.cancel()
        }
    ) {
        MaterialTheme(
            colors = AppTheme.Colors.material
        ) {
            rememberRootComponent { componentContext ->
                NavHost(componentContext)
            }.render()
        }
    }
}






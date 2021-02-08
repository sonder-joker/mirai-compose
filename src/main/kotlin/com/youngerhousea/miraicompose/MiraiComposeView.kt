package com.youngerhousea.miraicompose

import androidx.compose.desktop.Window
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntSize
import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.model.Model
import com.youngerhousea.miraicompose.theme.AppTheme
import com.youngerhousea.miraicompose.theme.ResourceImage
import com.youngerhousea.miraicompose.ui.MainWindowsView
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start

fun MiraiComposeView() {
    Window(
        title = "Mirai Compose",
        size = IntSize(1280, 768),
        icon = ResourceImage.icon,
    ) {
        MaterialTheme(
            colors = AppTheme.colors.material
        ) {
            val model = remember { Model() }
            Surface {
                MainWindowsView(model)
            }
        }
    }
}



package com.youngerhousea.miraicompose.ui

import androidx.compose.desktop.Window
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntSize
import com.youngerhousea.miraicompose.MiraiCompose
import com.youngerhousea.miraicompose.model.Model
import com.youngerhousea.miraicompose.theme.AppTheme
import com.youngerhousea.miraicompose.theme.ResourceImage
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start

fun MiraiComposeView() {
    MiraiCompose.start()
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
//                val hasTopBar by remember { mutableStateOf(false) }
//                if(hasTopBar) {
//                    TopAppBar {  }
//                }
                MainWindowsView(model)
            }
        }
    }
}


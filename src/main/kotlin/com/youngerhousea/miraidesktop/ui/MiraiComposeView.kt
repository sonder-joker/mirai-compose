/*
 *  此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU AFFERO GENERAL PUBLIC LICENSE version 3 license that can be found through the following link.
 *
 *  https://github.com/mamoe/mirai/blob/master/LICENSE
 */

package com.youngerhousea.miraidesktop.ui

import androidx.compose.desktop.Window
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntSize
import com.youngerhousea.miraidesktop.MiraiCompose
import com.youngerhousea.miraidesktop.model.Model
import com.youngerhousea.miraidesktop.theme.AppTheme
import com.youngerhousea.miraidesktop.theme.ResourceImage
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start
import net.mamoe.mirai.console.command.CommandManager

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
                MainWindowsView(model)
            }
        }
    }
}

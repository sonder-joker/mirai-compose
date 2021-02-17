package com.youngerhousea.miraicompose.ui

import androidx.compose.animation.Crossfade
import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.model.Model
import com.youngerhousea.miraicompose.model.PluginState
import com.youngerhousea.miraicompose.theme.AppTheme
import com.youngerhousea.miraicompose.theme.ResourceImage
import com.youngerhousea.miraicompose.ui.about.AboutWindow
import com.youngerhousea.miraicompose.ui.bot.BotsWindow
import com.youngerhousea.miraicompose.ui.log.LogWindow
import com.youngerhousea.miraicompose.ui.plugin.PluginsWindow
import com.youngerhousea.miraicompose.ui.setting.SettingWindow

fun MiraiComposeView() {
    Window(
        title = "Mirai Compose",
        size = IntSize(1280, 768),
        icon = ResourceImage.icon,
    ) {
        MaterialTheme(
            colors = AppTheme.Colors.material
        ) {
            Surface {
                Column {
                    MainWindowsView()
                }
            }
        }
    }
}

@Composable
private fun MainWindowsView() {
    val model = remember { Model() }
    val pluginState = remember { PluginState() }

    var currentWindow by remember { mutableStateOf(CurrentWindow.Bot) }
    Row {
        val settingWidth = 160.dp
        Column(
            Modifier
                .requiredWidth(settingWidth)
                .fillMaxHeight()
                .background(Color.DarkGray),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            SelectEdgeText(
                "机器人",
                if (currentWindow == CurrentWindow.Bot) AppTheme.Colors.backgroundDark else AppTheme.Colors.backgroundDarkGray
            ) {
                currentWindow = CurrentWindow.Bot
            }
            SelectEdgeText(
                "插件",
                if (currentWindow == CurrentWindow.Plugin) AppTheme.Colors.backgroundDark else AppTheme.Colors.backgroundDarkGray
            ) {
                currentWindow = CurrentWindow.Plugin
            }
            SelectEdgeText(
                "设置",
                if (currentWindow == CurrentWindow.Setting) AppTheme.Colors.backgroundDark else AppTheme.Colors.backgroundDarkGray
            ) { currentWindow = CurrentWindow.Setting }
            SelectEdgeText(
                "日志",
                if (currentWindow == CurrentWindow.Log) AppTheme.Colors.backgroundDark else AppTheme.Colors.backgroundDarkGray
            ) { currentWindow = CurrentWindow.Log }
            SelectEdgeText(
                "关于",
                if (currentWindow == CurrentWindow.About) AppTheme.Colors.backgroundDark else AppTheme.Colors.backgroundDarkGray
            ) { currentWindow = CurrentWindow.About }
        }
        Crossfade(targetState = currentWindow) { screen ->
            when (screen) {
                CurrentWindow.Bot -> BotsWindow(model)
                CurrentWindow.Plugin -> PluginsWindow(pluginState)
                CurrentWindow.Setting -> SettingWindow()
                CurrentWindow.About -> AboutWindow()
                CurrentWindow.Log -> LogWindow()
            }
        }
    }
}


@Composable
private fun SelectEdgeText(text: String, color: Color, onClick: () -> Unit) {

    Box(
        Modifier
            .clickable {
                onClick()
            }
            .fillMaxWidth()
            .requiredHeight(80.dp)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(text)
    }
    Divider(color = AppTheme.Colors.backgroundDark)
}

private enum class CurrentWindow {
    Bot, Setting, About, Plugin, Log
}
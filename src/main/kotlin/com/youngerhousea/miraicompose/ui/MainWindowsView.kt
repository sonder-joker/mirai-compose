package com.youngerhousea.miraicompose.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.model.Model
import com.youngerhousea.miraicompose.theme.AppTheme
import com.youngerhousea.miraicompose.ui.bot.BotsWindow
import com.youngerhousea.miraicompose.ui.plugin.PluginsWindow
import com.youngerhousea.miraicompose.ui.setting.SettingWindow

@Composable
fun MainWindowsView(model: Model) {
    var currentWindow by remember { mutableStateOf(CurrentWindow.Bot) }
    Row {
        val settingWidth = 160.dp
        Column(
            Modifier
                .preferredWidth(settingWidth)
                .fillMaxHeight()
                .background(Color.DarkGray),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SelectText(
                "机器人",
                if (currentWindow == CurrentWindow.Bot) AppTheme.colors.backgroundDark else Color.DarkGray
            ) {
                currentWindow = CurrentWindow.Bot
            }
            SelectText(
                "插件",
                if (currentWindow == CurrentWindow.Plugin) AppTheme.colors.backgroundDark else Color.DarkGray
            ) {
                currentWindow = CurrentWindow.Plugin
            }
            SelectText(
                "设置",
                if (currentWindow == CurrentWindow.Setting) AppTheme.colors.backgroundDark else Color.DarkGray
            ) { currentWindow = CurrentWindow.Setting }
            SelectText(
                "关于",
                if (currentWindow == CurrentWindow.About) AppTheme.colors.backgroundDark else Color.DarkGray
            ) { currentWindow = CurrentWindow.About }
        }
        Crossfade(targetState = currentWindow) { screen ->
            when (screen) {
                CurrentWindow.Bot -> BotsWindow(model)
                CurrentWindow.Plugin -> PluginsWindow(Modifier.fillMaxSize())
                CurrentWindow.Setting -> SettingWindow(Modifier.fillMaxSize())
                CurrentWindow.About -> AboutWindow(Modifier.fillMaxSize())
            }
        }
    }

}


@Composable
private fun SelectText(text: String, color: Color, onClick: () -> Unit) {
    val singleHeight = 80.dp
    Box(
        Modifier
            .clickable {
                onClick()
            }
            .fillMaxWidth()
            .preferredHeight(singleHeight)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(text)
    }
    Divider(color = AppTheme.colors.backgroundDark)
}

private enum class CurrentWindow {
    Bot, Setting, About, Plugin
}
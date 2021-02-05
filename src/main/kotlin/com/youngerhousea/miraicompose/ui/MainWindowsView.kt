package com.youngerhousea.miraicompose.ui

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
            SelectText({ currentWindow = CurrentWindow.Bot }) { Text("机器人") }
            SelectText({ currentWindow = CurrentWindow.Plugin }) { Text("插件") }
            SelectText({ currentWindow = CurrentWindow.Setting }) { Text("设置") }
            SelectText({ currentWindow = CurrentWindow.About }) { Text("关于") }
        }
        when (currentWindow) {
            CurrentWindow.Bot -> BotsWindow(model)
            CurrentWindow.Plugin -> PluginsWindow(Modifier.fillMaxSize())
            CurrentWindow.Setting -> SettingWindow(Modifier.fillMaxSize())
            CurrentWindow.About -> AboutWindow(Modifier.fillMaxSize())
        }

    }

}


@Composable
private fun SelectText(onClick: () -> Unit, content: @Composable() () -> Unit) {
    val singleHeight = 80.dp
    var currentColorTrue by remember(onClick) { mutableStateOf(false) }
    Box(
        Modifier
            .clickable {
                onClick()
                currentColorTrue = !currentColorTrue
            }
            .fillMaxWidth()
            .preferredHeight(singleHeight)
            .background(if (currentColorTrue) AppTheme.colors.backgroundDark else Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
    Divider(color = AppTheme.colors.backgroundDark)
}

private enum class CurrentWindow {
    Bot, Setting, About, Plugin
}
package com.youngerhousea.miraicompose.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.youngerhousea.miraicompose.theme.AppTheme

@Composable
fun SettingWindow() {
    Column(
        Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundDark)
    ) {
        Button({

        }) {
            Text("添加自动登录的机器人")
        }
    }
}
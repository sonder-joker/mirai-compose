package com.youngerhousea.miraicompose.ui.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.youngerhousea.miraicompose.console.MiraiCompose

@Composable
fun SettingWindow() {
    Column(
        Modifier
            .fillMaxSize()
    ) {
        MiraiCompose.loggerController
    }
}
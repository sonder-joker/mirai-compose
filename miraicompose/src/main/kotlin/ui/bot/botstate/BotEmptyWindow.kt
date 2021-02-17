package com.youngerhousea.miraicompose.ui.bot.botstate

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.youngerhousea.miraicompose.theme.ResourceImage

@Composable
fun BotEmptyWindow() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(ResourceImage.mirai, "mirai")
        Text("快去添加一个机器人吧")
    }
}

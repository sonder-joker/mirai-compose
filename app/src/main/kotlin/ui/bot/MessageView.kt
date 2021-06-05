package com.youngerhousea.miraicompose.ui.bot

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.youngerhousea.miraicompose.component.bot.Message


@Composable
fun MessageUi(message: Message) {
    Column {
        message.botList.forEach {
            Text("目前消息流量:${it.messageSpeed}")
        }
    }
}


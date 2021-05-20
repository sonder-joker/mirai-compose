package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.model.ComposeBot
import net.mamoe.mirai.console.permission.PermissionService
import java.awt.Desktop

/**
 * 在线bot的页面
 *
 */
class Message(
    componentContext: ComponentContext,
    val botList: List<ComposeBot>
) : ComponentContext by componentContext {
}

@Composable
fun MessageUi(message: Message) {
    Column {
        message.botList.forEach {
            Text("目前消息流量:${it.messageSpeed}")
        }
    }
}


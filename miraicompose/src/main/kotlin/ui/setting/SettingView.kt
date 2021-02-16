package com.youngerhousea.miraicompose.ui.setting

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.internal.data.builtins.AutoLoginConfig
import net.mamoe.mirai.utils.MiraiLogger

@Composable
fun SettingWindow() {
    Column(
        Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        Text("自定义配色(未实现)")
        SimpleSetWindows("VERBOSE") {  }
        SimpleSetWindows("INFO") {}
        SimpleSetWindows("WARING") {}
        SimpleSetWindows("ERROR") {}
        SimpleSetWindows("DEBUG") {}
    }
}

@Composable
private fun SimpleSetWindows(textValue: String, action: () -> Unit) {
    var textFieldValue by remember(textValue) { mutableStateOf("") }
    Row(
        Modifier
            .padding(vertical = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(textValue, Modifier.weight(2f), fontSize = 15.sp)
        Spacer(Modifier.weight(2f))
        TextField(textFieldValue, {
            textFieldValue = it
        }, Modifier.weight(2f).padding(end = 20.dp))
        Button(action) { Text("修改") }
    }
}

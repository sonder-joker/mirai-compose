package com.youngerhousea.miraicompose.ui.feature.setting

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.theme.AppTheme
import com.youngerhousea.miraicompose.utils.Component


internal class Setting(componentContext: ComponentContext, appTheme: AppTheme) : Component,
    ComponentContext by componentContext {
    @Composable
    override fun render() {
        Column(
            Modifier
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Text("自定义配色(未实现)")

//            SimpleSetWindows("VERBOSE") { logColor.verbose = Color(it.toULong(16)) }
//            SimpleSetWindows("INFO") { logColor.info = Color(it.toULong(16)) }
//            SimpleSetWindows("WARING") { logColor.warning = Color(it.toULong(16)) }
//            SimpleSetWindows("ERROR") { logColor.error = Color(it.toULong(16)) }
//            SimpleSetWindows("DEBUG") { logColor.debug = Color(it.toULong(16)) }
        }
    }
}

@Composable
private fun SimpleSetWindows(textValue: String, action: (value: String) -> Unit) {
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
        Button({ action(textFieldValue) }) { Text("修改") }
    }
}

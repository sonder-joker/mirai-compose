package com.youngerhousea.miraicompose.ui.feature.setting

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.darkColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.console.ComposeDataScope
import com.youngerhousea.miraicompose.theme.ComposeSetting
import kotlinx.coroutines.launch


class Setting(
    componentContext: ComponentContext
) : ComponentContext by componentContext {

}

@Composable
fun SettingUi() {
    Column(
        Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        Text("自定义日志配色")
        SimpleSetWindows("VERBOSE") {
            ComposeDataScope.launch {
                ComposeSetting.AppTheme.logColors.verbose = Color(it.removePrefix("0x").toULong(16))
            }
        }
        SimpleSetWindows("INFO") {
            ComposeDataScope.launch {
                ComposeSetting.AppTheme.logColors.info = Color(it.removePrefix("0x").toULong(16))
            }
        }
        SimpleSetWindows("WARING") {
            ComposeDataScope.launch {
                ComposeSetting.AppTheme.logColors.warning = Color(it.removePrefix("0x").toULong(16))
            }
        }
        SimpleSetWindows("ERROR") {
            ComposeDataScope.launch {
                ComposeSetting.AppTheme.logColors.error = Color(it.removePrefix("0x").toULong(16))
            }
        }
        SimpleSetWindows("DEBUG") {
            ComposeDataScope.launch {
                ComposeSetting.AppTheme.logColors.debug = Color(it.removePrefix("0x").toULong(16))
            }
        }
        SimpleSetWindows("Primary") {
            ComposeDataScope.launch {
                ComposeSetting.AppTheme.themeColors.materialLight = darkColors()
            }
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

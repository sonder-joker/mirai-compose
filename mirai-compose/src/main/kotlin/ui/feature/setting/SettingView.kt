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

@Suppress("NOTHING_TO_INLINE")
private inline fun String.toColor() = Color(this.removePrefix("0x").toULong(16))

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
            ComposeSetting.AppTheme.logColors.verbose = it.toColor()
        }
        SimpleSetWindows("INFO") {
            ComposeSetting.AppTheme.logColors.info = it.toColor()
        }
        SimpleSetWindows("WARING") {
            ComposeSetting.AppTheme.logColors.warning = it.toColor()
        }
        SimpleSetWindows("ERROR") {
            ComposeSetting.AppTheme.logColors.error = it.toColor()
        }

        SimpleSetWindows("DEBUG") {
            ComposeSetting.AppTheme.logColors.debug = it.toColor()
        }
        SimpleSetWindows("Primary") {
            ComposeSetting.AppTheme.materialLight =
                ComposeSetting.AppTheme.materialLight
                    .copy(primary = it.toColor())
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
        Button({
            ComposeDataScope.launch {
                action(textFieldValue)
            }
        }) { Text("修改") }
    }
}

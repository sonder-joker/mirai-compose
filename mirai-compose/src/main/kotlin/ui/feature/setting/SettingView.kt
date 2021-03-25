package com.youngerhousea.miraicompose.ui.feature.setting

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.console.ComposeDataScope
import com.youngerhousea.miraicompose.theme.ComposeSetting
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

@Suppress("NOTHING_TO_INLINE")
private inline fun String.toColor() = Color(this.removePrefix("#").toULong(16))

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

        SimpleSetWindows("onPrimary") {
            ComposeSetting.AppTheme.materialLight =
                ComposeSetting.AppTheme.materialLight
                    .copy(onPrimary = it.toColor())
        }

    }
}

@Composable
private fun SimpleSetWindows(textValue: String, action: (value: String) -> Unit) {
    var textFieldValue by remember(textValue) { mutableStateOf("") }
    var errorTip by remember(textValue) { mutableStateOf("") }
    var isError by remember(textValue) { mutableStateOf(false) }
    Row(
        Modifier
            .padding(vertical = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(textValue, Modifier.weight(2f), fontSize = 15.sp)
        Spacer(Modifier.weight(2f))
        TextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
            },
            modifier = Modifier
                .weight(2f)
                .padding(end = 20.dp),
            isError = isError,
            label = {
                Text(errorTip)
            }
        )
        Button({
            ComposeDataScope.launch {
                runCatching {
                    action(textFieldValue)
                }.onFailure {
                    errorTip = if (it is NumberFormatException) {
                        "Wrong string"
                    } else {
                        "Unknown string"
                    }
                    isError = true
                    delay(3 * 1000)
                    isError = false
                    errorTip = ""
                }
            }
        }) { Text("修改") }
    }
}

package com.youngerhousea.miraicompose.ui.feature.setting

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.console.ComposeDataScope
import com.youngerhousea.miraicompose.theme.ComposeSetting
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Suppress("NOTHING_TO_INLINE")
private inline fun String.toColor() = Color(this.removePrefix("#").toULong(16))

class Setting(
    componentContext: ComponentContext
) : ComponentContext by componentContext

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettingUi() {
    Box(Modifier.fillMaxSize()) {
        val scrollState = rememberScrollState()
        Column(
            Modifier
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Row(Modifier.fillMaxWidth()) {
                Text("自定义日志配色")
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                SimpleSetWindows("VERBOSE", ComposeSetting.AppTheme.logColors.verbose) {
                    ComposeSetting.AppTheme.logColors.verbose = it.toColor()
                }
                SimpleSetWindows("INFO", ComposeSetting.AppTheme.logColors.info) {
                    ComposeSetting.AppTheme.logColors.info = it.toColor()
                }
                SimpleSetWindows("WARING", ComposeSetting.AppTheme.logColors.warning) {
                    ComposeSetting.AppTheme.logColors.warning = it.toColor()
                }
                SimpleSetWindows("ERROR", ComposeSetting.AppTheme.logColors.error) {
                    ComposeSetting.AppTheme.logColors.error = it.toColor()
                }
                SimpleSetWindows("DEBUG", ComposeSetting.AppTheme.logColors.debug) {
                    ComposeSetting.AppTheme.logColors.debug = it.toColor()
                }
//                SimpleSetWindows("Primary",) {
//                    ComposeSetting.AppTheme.materialLight =
//                        ComposeSetting.AppTheme.materialLight
//                            .copy(primary = it.toColor())
//                }
//                SimpleSetWindows("onPrimary",) {
//                    ComposeSetting.AppTheme.materialLight =
//                        ComposeSetting.AppTheme.materialLight
//                            .copy(onPrimary = it.toColor())
//                }
            }
        }
        VerticalScrollbar(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            adapter = ScrollbarAdapter(scrollState)
        )
    }
}

@Composable
private fun SimpleSetWindows(textValue: String, color: Color, action: (value: String) -> Unit) {
    var textFieldValue by remember(textValue) { mutableStateOf("") }
    var errorTip by remember(textValue) { mutableStateOf("") }
    var isError by remember(textValue) { mutableStateOf(false) }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .height(40.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(textValue, Modifier.weight(2f), fontSize = 15.sp)
        Spacer(Modifier.weight(2f))
        ColorImage(color, null, Modifier.weight(1f))
        Spacer(Modifier.weight(1f))
        ColorImage(Color.Black, null, Modifier.weight(1f))
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

@Composable
fun ColorImage(color: Color, contentDescription: String?, modifier: Modifier = Modifier) =
    Image(painter = ColorPainter(color), contentDescription, modifier)
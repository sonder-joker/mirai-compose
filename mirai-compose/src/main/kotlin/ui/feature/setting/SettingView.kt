package com.youngerhousea.miraicompose.ui.feature.setting

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.shortcuts
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.theme.ComposeSetting
import com.youngerhousea.miraicompose.utils.IntSlider
import com.youngerhousea.miraicompose.utils.getARGB
import java.util.*

@Suppress("NOTHING_TO_INLINE")
private inline fun String.toColor(): Color = run {
    // r, g, b or a, r, g, b
    val tmp: List<Int> =
        if (this.contains(',')) {
            this.split(',').map { it.toInt() }
        } else if (this.startsWith('#')) {
            getARGB(this.removePrefix("#")).toList()
        } else {
            throw InputMismatchException()
        }
    return if (tmp.count() == 4) {
        //argb
        Color(tmp[1], tmp[2], tmp[3], tmp[0])
    } else if (tmp.count() == 3) {
        //rgb
        Color(tmp[0], tmp[1], tmp[2])
    } else {
        throw InputMismatchException()
    }
}


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
                Text("自定义主题配色")
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                SlideSetWindow()
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
fun SlideSetWindow() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .height(40.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Verbose")
        IntSlider(
            value = ComposeSetting.AppTheme.logColors.verbose.toArgb(),
            onValueChange = {
                println(it)
                ComposeSetting.AppTheme.logColors.verbose = Color(it)
            },
            valueRange = 0..0xffffff,
            colors = SliderDefaults.colors(thumbColor = ComposeSetting.AppTheme.logColors.verbose)
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
                .padding(end = 20.dp)
                .shortcuts {
                    on(Key.Enter) {
                        //TODO click the button
                    }
                },
            isError = isError,
            label = {
                Text(errorTip)
            }
        )

        Button({
            runCatching {
                action(textFieldValue)
            }.onFailure {
                errorTip = when (it) {
                    is NumberFormatException -> {
                        "Wrong string"
                    }
                    is InputMismatchException -> {
                        "Wrong formation"
                    }
                    else -> {
                        "Unknown string"
                    }
                }
//                isError = true
//                delay(1000)
//                isError = false
//                errorTip = ""
            }.onSuccess {
                // TODO change the color of tip to green
//                errorTip = "Success"
//                isError = true
//                delay(1000)
//                isError = false
//                errorTip = ""
            }
        }) { Text("修改") }
    }
}

@Composable
fun ColorImage(color: Color, contentDescription: String?, modifier: Modifier = Modifier) =
    Image(painter = ColorPainter(color), contentDescription, modifier)
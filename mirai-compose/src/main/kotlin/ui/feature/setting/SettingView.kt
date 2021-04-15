package com.youngerhousea.miraicompose.ui.feature.setting

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.theme.ComposeSetting
import com.youngerhousea.miraicompose.utils.getARGB
import net.mamoe.mirai.console.data.PluginData
import okhttp3.internal.toHexString
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
    componentContext: ComponentContext,
    data: List<PluginData>,
) : ComponentContext by componentContext {
    val debug get() = ComposeSetting.AppTheme.logColors.debug

    val verbose get() = ComposeSetting.AppTheme.logColors.verbose

    val info get() = ComposeSetting.AppTheme.logColors.info

    val warning get() = ComposeSetting.AppTheme.logColors.warning

    val error get() = ComposeSetting.AppTheme.logColors.error

    val primary get() = ComposeSetting.AppTheme.materialLight.primary

    val onPrimary get() = ComposeSetting.AppTheme.materialLight.onPrimary

    val secondary get() = ComposeSetting.AppTheme.materialLight.secondary

    val onSecondary get() = ComposeSetting.AppTheme.materialLight.onSecondary

    val surface get() = ComposeSetting.AppTheme.materialLight.surface

    val onSurface get() = ComposeSetting.AppTheme.materialLight.onSurface

    fun onDebugColorSet(color: Color) {
        ComposeSetting.AppTheme.logColors.debug = color
    }

    fun onVerboseColorSet(color: Color) {
        ComposeSetting.AppTheme.logColors.verbose = color
    }

    fun onInfoColorSet(color: Color) {
        ComposeSetting.AppTheme.logColors.info = color
    }

    fun onWarningColorSet(color: Color) {
        ComposeSetting.AppTheme.logColors.warning = color
    }

    fun onErrorColorSet(color: Color) {
        ComposeSetting.AppTheme.logColors.error = color
    }

    fun onPrimaryColorSet(color: Color) {
        ComposeSetting.AppTheme.materialLight = ComposeSetting.AppTheme.materialLight.copy(primary = color)
    }

    fun onOnPrimaryColorSet(color: Color) {
        ComposeSetting.AppTheme.materialLight = ComposeSetting.AppTheme.materialLight.copy(onPrimary = color)
    }

    fun onSecondaryColorSet(color: Color) {
        ComposeSetting.AppTheme.materialLight = ComposeSetting.AppTheme.materialLight.copy(secondary = color)
    }

    fun onOnSecondaryColorSet(color: Color) {
        ComposeSetting.AppTheme.materialLight = ComposeSetting.AppTheme.materialLight.copy(onSecondary = color)
    }

    fun onSurfaceColorSet(color: Color) {
        ComposeSetting.AppTheme.materialLight = ComposeSetting.AppTheme.materialLight.copy(surface = color)
    }

    fun onOnSurfaceColorSet(color: Color) {
        ComposeSetting.AppTheme.materialLight = ComposeSetting.AppTheme.materialLight.copy(onSurface = color)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettingUi(setting: Setting) {
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
                ColorSetSlider("Debug", value = setting.debug, onValueChange = setting::onDebugColorSet)
                ColorSetSlider("Verbose", setting.verbose, onValueChange = setting::onVerboseColorSet)
                ColorSetSlider("Info", setting.info, onValueChange = setting::onInfoColorSet)
                ColorSetSlider("Warning", setting.warning, onValueChange = setting::onWarningColorSet)
                ColorSetSlider("Error", setting.error, onValueChange = setting::onErrorColorSet)
                ColorSetSlider("Primary", setting.primary, onValueChange = setting::onPrimaryColorSet)
                ColorSetSlider("OnPrimary", setting.onPrimary, onValueChange = setting::onOnPrimaryColorSet)
                ColorSetSlider("Secondary", setting.secondary, onValueChange = setting::onSecondaryColorSet)
                ColorSetSlider("OnSecondary", setting.onSecondary, onValueChange = setting::onOnSecondaryColorSet)
                ColorSetSlider("Surface", setting.surface, onValueChange = setting::onSurfaceColorSet)
                ColorSetSlider("OnSurface", setting.onSurface, onValueChange = setting::onOnSurfaceColorSet)
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
fun ColorSetSlider(text: String, value: Color, onValueChange: (Color) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .height(40.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(ColorPainter(value), null, Modifier.width(200.dp))
        OutlinedTextField(value.toArgb().toHexString(), onValueChange = {
            onValueChange(Color(it.removePrefix("#").toInt(16)))
        })
//        Button({
//            onValueChange(Color(it.removePrefix("#").toInt(16)))
//        }){
//            Text("Sure")
//        }
//        IntSlider(
//            value = value.toArgb(),
//            onValueChange = { onValueChange(Color(it)) },
//            valueRange = 0..0xffffff,
//            colors = SliderDefaults.colors(activeTrackColor = value)
//        )
    }
}

@Composable
fun ColorSpliter() {
    Canvas(Modifier) {

    }
}

//@Composable
//private fun SimpleSetWindows(textValue: String, color: Color, action: (value: String) -> Unit) {
//    var textFieldValue by remember(textValue) { mutableStateOf("") }
//    var errorTip by remember(textValue) { mutableStateOf("") }
//    var isError by remember(textValue) { mutableStateOf(false) }
//
//    Row(
//        Modifier
//            .fillMaxWidth()
//            .padding(vertical = 10.dp)
//            .height(40.dp),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text(textValue, Modifier.weight(2f), fontSize = 15.sp)
//        Spacer(Modifier.weight(2f))
//        ColorImage(color, null, Modifier.weight(1f))
//        Spacer(Modifier.weight(1f))
//        ColorImage(Color.Black, null, Modifier.weight(1f))
//        Spacer(Modifier.weight(2f))
//        TextField(
//            value = textFieldValue,
//            onValueChange = {
//                textFieldValue = it
//            },
//            modifier = Modifier
//                .weight(2f)
//                .padding(end = 20.dp)
//                .shortcuts {
//                    on(Key.Enter) {
//                        //TODO click the button
//                    }
//                },
//            isError = isError,
//            label = {
//                Text(errorTip)
//            }
//        )
//
//        Button({
//            runCatching {
//                action(textFieldValue)
//            }.onFailure {
//                errorTip = when (it) {
//                    is NumberFormatException -> {
//                        "Wrong string"
//                    }
//                    is InputMismatchException -> {
//                        "Wrong formation"
//                    }
//                    else -> {
//                        "Unknown string"
//                    }
//                }
////                isError = true
////                delay(1000)
////                isError = false
////                errorTip = ""
//            }.onSuccess {
//                // TODO change the color of tip to green
////                errorTip = "Success"
////                isError = true
////                delay(1000)
////                isError = false
////                errorTip = ""
//            }
//        }) { Text("修改") }
//    }
//}
//
//@Composable
//fun ColorImage(color: Color, contentDescription: String?, modifier: Modifier = Modifier) =
//    Image(painter = ColorPainter(color), contentDescription, modifier)
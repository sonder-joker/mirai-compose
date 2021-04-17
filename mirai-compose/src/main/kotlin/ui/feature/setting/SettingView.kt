package com.youngerhousea.miraicompose.ui.feature.setting

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
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
    var isExpand by remember(text) { mutableStateOf(false) }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .height(40.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.width(200.dp)) {
            Text(text)
        }

        Image(ColorPainter(value), null, Modifier.width(200.dp))

        if (isExpand)
            Popup {

            }
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

}

private suspend fun AwaitPointerEventScope.awaitEventFirstDown(): PointerEvent {
    var event: PointerEvent
    do {
        event = awaitPointerEvent()
    } while (
        !event.changes.all { it.changedToDown() }
    )
    return event
}

private fun DrawScope.drawBackground(x: Int, y: Int, color: Color, widthRate: Int, heightRate: Int) {
    drawRect(
        color = color,
        topLeft = Offset(x = x * widthRate.toFloat(), y = y * heightRate.toFloat()),
        size = Size(widthRate.toFloat(), heightRate.toFloat())
    )
}
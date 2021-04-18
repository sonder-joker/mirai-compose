package com.youngerhousea.miraicompose.ui.feature.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.theme.AppTheme
import com.youngerhousea.miraicompose.theme.ComposeSetting
import com.youngerhousea.miraicompose.ui.common.ColorPicker
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
    componentContext: ComponentContext,
    val theme: AppTheme,
) : ComponentContext by componentContext {
    val debug get() = ComposeSetting.AppTheme.logColors.debug

    val verbose get() = ComposeSetting.AppTheme.logColors.verbose

    val info get() = ComposeSetting.AppTheme.logColors.info

    val warning get() = ComposeSetting.AppTheme.logColors.warning

    val error get() = ComposeSetting.AppTheme.logColors.error

    val primary get() = ComposeSetting.AppTheme.materialLight.primary

    val primaryVariant get() = ComposeSetting.AppTheme.materialLight.primaryVariant

    val secondary get() = ComposeSetting.AppTheme.materialLight.secondary

    val secondaryVariant get() = ComposeSetting.AppTheme.materialLight.secondaryVariant

    val background get() = ComposeSetting.AppTheme.materialLight.background

    val surface get() = ComposeSetting.AppTheme.materialLight.surface

    val onPrimary get() = ComposeSetting.AppTheme.materialLight.onPrimary

    val onSecondary get() = ComposeSetting.AppTheme.materialLight.onSecondary

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

    fun setPrimaryColor(color: Color) {
        ComposeSetting.AppTheme.materialLight = ComposeSetting.AppTheme.materialLight.copy(primary = color)
    }

    fun setPrimaryVariant(color: Color) {
        ComposeSetting.AppTheme.materialLight = ComposeSetting.AppTheme.materialLight.copy(primaryVariant = color)
    }

    fun setSecondaryColor(color: Color) {
        ComposeSetting.AppTheme.materialLight = ComposeSetting.AppTheme.materialLight.copy(secondary = color)
    }

    fun onSecondaryVariant(color: Color) {
        ComposeSetting.AppTheme.materialLight = ComposeSetting.AppTheme.materialLight.copy(secondaryVariant = color)
    }

    fun setOnBackground(color: Color) {
        ComposeSetting.AppTheme.materialLight = ComposeSetting.AppTheme.materialLight.copy(background = color)
    }

    fun setSurfaceColor(color: Color) {
        ComposeSetting.AppTheme.materialLight = ComposeSetting.AppTheme.materialLight.copy(surface = color)
    }

    fun setError(color: Color) {
        ComposeSetting.AppTheme.materialLight = ComposeSetting.AppTheme.materialLight.copy(error = color)
    }

    fun setOnPrimaryColor(color: Color) {
        ComposeSetting.AppTheme.materialLight = ComposeSetting.AppTheme.materialLight.copy(onPrimary = color)
    }

    fun setOnSecondaryColor(color: Color) {
        ComposeSetting.AppTheme.materialLight = ComposeSetting.AppTheme.materialLight.copy(onSecondary = color)
    }

    fun setOnBackgroundColor(color: Color) {
        ComposeSetting.AppTheme.materialLight = ComposeSetting.AppTheme.materialLight.copy(onBackground = color)
    }

    fun setOnSurfaceColor(color: Color) {
        ComposeSetting.AppTheme.materialLight = ComposeSetting.AppTheme.materialLight.copy(onSurface = color)
    }

    fun setOnErrorColor(color: Color) {
        ComposeSetting.AppTheme.materialLight = ComposeSetting.AppTheme.materialLight.copy(onError = color)
    }

    fun setIsLight(isLight: Boolean) {
        ComposeSetting.AppTheme.materialLight = ComposeSetting.AppTheme.materialLight.copy(isLight = isLight)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettingUi(setting: Setting) {
    Box(Modifier.fillMaxSize()) {
        val scrollState = rememberScrollState()
        Column(
            Modifier
                .verticalScroll(scrollState)
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Row(Modifier.fillMaxWidth()) {
                Text("自定义主题配色")
            }
            ColorSetSlider("Debug", value = setting.debug, onValueChange = setting::onDebugColorSet)
            ColorSetSlider("Verbose", setting.verbose, onValueChange = setting::onVerboseColorSet)
            ColorSetSlider("Info", setting.info, onValueChange = setting::onInfoColorSet)
            ColorSetSlider("Warning", setting.warning, onValueChange = setting::onWarningColorSet)
            ColorSetSlider("Error", setting.error, onValueChange = setting::onErrorColorSet)
            ColorSetSlider("Primary", setting.primary, onValueChange = setting::setPrimaryColor)
            ColorSetSlider("OnPrimary", setting.onPrimary, onValueChange = setting::setOnPrimaryColor)
            ColorSetSlider("Secondary", setting.secondary, onValueChange = setting::setSecondaryColor)
            ColorSetSlider("OnSecondary", setting.onSecondary, onValueChange = setting::setOnSecondaryColor)
            ColorSetSlider("Surface", setting.surface, onValueChange = setting::setSurfaceColor)
            ColorSetSlider("OnSurface", setting.onSurface, onValueChange = setting::setOnSurfaceColor)
            ColorSetSlider("OnSurface", setting.onSurface, onValueChange = setting::setOnSurfaceColor)
            ColorSetSlider("OnSurface", setting.onSurface, onValueChange = setting::setOnSurfaceColor)
            ColorSetSlider("OnSurface", setting.onSurface, onValueChange = setting::setOnSurfaceColor)
            ColorSetSlider("OnSurface", setting.onSurface, onValueChange = setting::setOnSurfaceColor)
            ColorSetSlider("OnSurface", setting.onSurface, onValueChange = setting::setOnSurfaceColor)
        }
        VerticalScrollbar(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            adapter = ScrollbarAdapter(scrollState)
        )
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ColorSetSlider(text: String, value: Color, onValueChange: (Color) -> Unit) {
    var isExpand by remember(text) { mutableStateOf(false) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
            Button({
                isExpand = !isExpand
            }) {
                Text("?")
            }
        }
        AnimatedVisibility(isExpand) {
            ColorPicker(value) { red, green, blue, alpha ->
                onValueChange(Color(red.toFloat(), green.toFloat(), blue.toFloat(), alpha))
            }
        }
    }

}

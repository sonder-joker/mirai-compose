package com.youngerhousea.miraicompose.ui.feature.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.theme.AppTheme
import com.youngerhousea.miraicompose.ui.common.ColorPicker

/**
 * Compose各项参数的设置
 *
 * TODO:提供注释
 */
class Setting(
    componentContext: ComponentContext,
    val theme: AppTheme
) : ComponentContext by componentContext {
    private inline val logColors get() = theme.logColors

    var material = theme.materialLight
        set(value) {
            theme.materialLight = value
            field = value
        }

    val debug get() = logColors.debug

    val verbose get() = logColors.verbose

    val info get() = logColors.info

    val warning get() = logColors.warning

    val error get() = logColors.error

    fun onDebugColorSet(color: Color) {
        logColors.debug = color
    }

    fun onVerboseColorSet(color: Color) {
        logColors.verbose = color
    }

    fun onInfoColorSet(color: Color) {
        logColors.info = color
    }

    fun onWarningColorSet(color: Color) {
        logColors.warning = color
    }

    fun onErrorColorSet(color: Color) {
        logColors.error = color
    }

    fun setPrimary(color: Color) {
        material = material.copy(primary = color)
    }

    fun setPrimaryVariant(color: Color) {
        material = material.copy(primaryVariant = color)
    }

    fun setSecondary(color: Color) {
        material = material.copy(secondary = color)
    }

    fun setSecondaryVariant(color: Color) {
        material = material.copy(secondaryVariant = color)
    }

    fun setBackground(color: Color) {
        material = material.copy(background = color)
    }

    fun setSurface(color: Color) {
        material = material.copy(surface = color)
    }

    fun setError(color: Color) {
        material = material.copy(error = color)
    }

    fun setOnPrimary(color: Color) {
        material = material.copy(onPrimary = color)
    }

    fun setOnSecondary(color: Color) {
        material = material.copy(onSecondary = color)
    }

    fun setOnBackground(color: Color) {
        material = material.copy(onBackground = color)
    }

    fun setOnSurface(color: Color) {
        material = material.copy(onSurface = color)
    }

    fun setOnError(color: Color) {
        material = material.copy(onError = color)
    }

    fun setIsLight(isLight: Boolean) {
        material = material.copy(isLight = isLight)
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
                Text("自定义日志配色")
            }
            ColorSetSlider("Debug", value = setting.debug, onValueChange = setting::onDebugColorSet)
            ColorSetSlider("Verbose", setting.verbose, onValueChange = setting::onVerboseColorSet)
            ColorSetSlider("Info", setting.info, onValueChange = setting::onInfoColorSet)
            ColorSetSlider("Warning", setting.warning, onValueChange = setting::onWarningColorSet)
            ColorSetSlider("Error", setting.error, onValueChange = setting::onErrorColorSet)
//            TODO:With a more good way
//            Row(Modifier.fillMaxWidth()) {
//                Text("自定义主题配色")
//            }
//            ColorSetSlider("Primary", setting.material.primary, onValueChange = setting::setPrimary)
//            ColorSetSlider(
//                "PrimaryVariant",
//                setting.material.primaryVariant,
//                onValueChange = setting::setPrimaryVariant
//            )
//            ColorSetSlider("Secondary", setting.material.secondary, onValueChange = setting::setSecondary)
//            ColorSetSlider(
//                "SecondaryVariant",
//                setting.material.secondaryVariant,
//                onValueChange = setting::setSecondaryVariant
//            )
//            ColorSetSlider("Background", setting.material.background, onValueChange = setting::setBackground)
//            ColorSetSlider("Surface", setting.material.surface, onValueChange = setting::setSurface)
//            ColorSetSlider("Error", setting.material.error, onValueChange = setting::setError)
//            ColorSetSlider("OnPrimary", setting.material.onPrimary, onValueChange = setting::setOnPrimary)
//            ColorSetSlider("OnSecondary", setting.material.onSecondary, onValueChange = setting::setOnSecondary)
//            ColorSetSlider("OnBackground", setting.material.onBackground, onValueChange = setting::setOnBackground)
//            ColorSetSlider("OnSurface", setting.material.onSurface, onValueChange = setting::setOnSurface)
//            ColorSetSlider("OnError", setting.material.onError, onValueChange = setting::setOnError)
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

            Button({
                isExpand = !isExpand
            }) {
                Text("#" + value.value.toString(16).substring(0, 8))
            }
        }
        AnimatedVisibility(isExpand) {
            ColorPicker(value) { red, green, blue, alpha ->
                onValueChange(Color(red, green, blue, alpha))
            }
        }
    }
}

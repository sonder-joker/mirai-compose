package com.youngerhousea.miraicompose.app.ui.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.app.utils.ColorSerializer
import com.youngerhousea.miraicompose.core.component.setting.LogColorSetting
import net.mamoe.yamlkt.Yaml

@Composable
fun LogColorSettingUi(setting: LogColorSetting) {
    val logColor by setting.logColor.collectAsState()

    val debug by derivedStateOf { Yaml.decodeFromString(ColorSerializer, logColor.debug) }

    val verbose by derivedStateOf { Yaml.decodeFromString(ColorSerializer, logColor.verbose) }

    val info by derivedStateOf { Yaml.decodeFromString(ColorSerializer, logColor.info) }

    val warning by derivedStateOf { Yaml.decodeFromString(ColorSerializer, logColor.warning) }

    val error by derivedStateOf { Yaml.decodeFromString(ColorSerializer, logColor.error) }

    Scaffold(
        topBar = {
            Icon(Icons.Default.KeyboardArrowLeft, null, Modifier.clickable(onClick = setting::onExitButtonClicked))
        }
    ) {
        Column(Modifier.fillMaxSize()) {
            Row {
                Text("自定义日志配色")
            }
            ColorSetSlider("Debug", debug, onValueChange = {
                setting.setDebugColor(Yaml.encodeToString(ColorSerializer, it))
            })
            ColorSetSlider("Verbose", verbose, onValueChange = {
                setting.setVerboseColor(Yaml.encodeToString(ColorSerializer, it))
            })
            ColorSetSlider("Info", info, onValueChange = {
                setting.setInfoColor(Yaml.encodeToString(ColorSerializer, it))
            })
            ColorSetSlider("Warning", warning, onValueChange = {
                setting.setWarningColor(Yaml.encodeToString(ColorSerializer, it))
            })
            ColorSetSlider("Error", error, onValueChange = {
                setting.setErrorColor(Yaml.encodeToString(ColorSerializer, it))
            })
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ColorSetSlider(text: String, color: Color, onValueChange: (Color) -> Unit) {
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

            Button(onClick = {
                isExpand = !isExpand
            }, colors = ButtonDefaults.buttonColors(backgroundColor = color), modifier = Modifier.width(200.dp)) {
                Text(Yaml.encodeToString(ColorSerializer, color))
            }
        }
        AnimatedVisibility(isExpand) {
            ColorPicker(color) { color ->
                onValueChange(color)
            }
        }
    }
}

@Composable
@Suppress("DuplicatedCode")
fun ColorPicker(
    color: Color,
    onColorChange: (color: Color) -> Unit
) {
    Column {
        Slider(color.alpha, {
            onColorChange(color.copy(alpha = it))
        }, valueRange = 0f..1f)

        Slider(color.red, {
            onColorChange(color.copy(red = it))
        }, valueRange = 0f..1f)

        Slider(color.green, {
            onColorChange(color.copy(green = it))
        }, valueRange = 0f..1f)

        Slider(color.blue, {
            onColorChange(color.copy(blue = it))
        }, valueRange = 0f..1f)
    }
}

//need more optimization
@Composable
fun ColorPicker(
    color: Color,
    firstCanvasWidth: Float = 20f,
    firstCanvasHeight: Float = 1f,
    secondCanvasSingleElementSize: Float = 1f,
    onColorChange: (color: Color) -> Unit
) {
    // pointer move rgb
    var red by remember { mutableStateOf(color.red) }
    var green by remember { mutableStateOf(color.green) }
    var blue by remember { mutableStateOf(color.blue) }
    var alpha by remember { mutableStateOf(color.alpha) }

    Row(Modifier.fillMaxSize().animateContentSize(), horizontalArrangement = Arrangement.SpaceEvenly) {

        Canvas(modifier = Modifier.pointerMoveFilter(onMove = { position ->
            red = (position.y / firstCanvasHeight) / 255f
            true
        }).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
            Color(red, green, blue, alpha)
        }.size(firstCanvasWidth.dp, (256 * firstCanvasHeight).dp)) {
            for (x in 0..255) {
                drawBackground(0f, x.toFloat(), Color(x / 255f, 0f, 0f, alpha), firstCanvasWidth, firstCanvasHeight)
            }
        }

        Canvas(modifier = Modifier.pointerMoveFilter(onMove = { position ->
            blue = position.x / secondCanvasSingleElementSize / 255f
            green = position.y / secondCanvasSingleElementSize / 255f
            true
        }).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
            onColorChange(Color(red, green, blue, alpha))
        }.size((secondCanvasSingleElementSize * 256).dp)) {
            for (x in 0..255) {
                for (y in 0..255) {
                    drawBackground(
                        x / 255f,
                        y / 255f,
                        Color(red, y / 255f, x / 255f, alpha),
                        secondCanvasSingleElementSize,
                        secondCanvasSingleElementSize
                    )
                }
            }
        }

        Slider(alpha, {
            alpha = it
            onColorChange(Color(red, green, blue, alpha))
        }, valueRange = 0f..1f, modifier = Modifier.width(100.dp))

        Row {
            Text("Pointer color")
            Image(ColorPainter(Color(red, green, blue, alpha)), null, Modifier.width(20.dp).height(20.dp))
        }

        Row {
            Text("Now color")
            Image(
                ColorPainter(color),
                null,
                Modifier.width(20.dp).height(20.dp)
            )
        }

    }
}


private fun DrawScope.drawBackground(x: Float, y: Float, color: Color, widthRate: Float, heightRate: Float) {
    drawRect(
        color = color,
        topLeft = Offset(x = x * widthRate, y = y * heightRate),
        size = Size(widthRate, heightRate)
    )
}


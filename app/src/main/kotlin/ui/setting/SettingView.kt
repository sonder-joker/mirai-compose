package com.youngerhousea.miraicompose.app.ui.setting

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import com.youngerhousea.miraicompose.app.ui.plugin.DetailedDataUi
import com.youngerhousea.miraicompose.app.ui.plugin.EditView
import com.youngerhousea.miraicompose.core.component.setting.Setting
import com.youngerhousea.miraicompose.core.component.setting.StringColor
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.logging.AbstractLoggerController

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SettingUi(setting: Setting) {
    val (debug, setDebug) = remember { mutableStateOf(setting.debug) }
    val (verbose, setVerbose) = remember { mutableStateOf(setting.verbose) }
    val (info, setInfo) = remember { mutableStateOf(setting.info) }
    val (warning, setWarning) = remember { mutableStateOf(setting.warning) }
    val (error, setError) = remember { mutableStateOf(setting.error) }

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
            ColorSetSlider("Debug", debug, onValueChange = {
                setDebug(it)
                setting.onDebugColorSet(debug)
            })
            ColorSetSlider("Verbose", verbose, onValueChange = {
                setVerbose(it)
                setting.onVerboseColorSet(verbose)
            })
            ColorSetSlider("Info", info, onValueChange = {
                setInfo(it)
                setting.onInfoColorSet(info)
            })
            ColorSetSlider("Warning", warning, onValueChange = {
                setWarning(it)
                setting.onWarningColorSet(warning)
            })
            ColorSetSlider("Error", error, onValueChange = {
                setError(it)
                setting.onErrorColorSet(error)
            })
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

            var state by remember { mutableStateOf(0) }

            Row {
                Text("Log level", Modifier.weight(4f))
                TabRow(state, Modifier.weight(1f)) {
                    Tab(setting.logConfigLevel == AbstractLoggerController.LogPriority.ALL, onClick = {
                        state = 0
                        setting.setLogConfigLevel(AbstractLoggerController.LogPriority.ALL)
                    })
                    Tab(setting.logConfigLevel == AbstractLoggerController.LogPriority.VERBOSE, onClick = {
                        state = 1
                        setting.setLogConfigLevel(AbstractLoggerController.LogPriority.VERBOSE)
                    })
                    Tab(setting.logConfigLevel == AbstractLoggerController.LogPriority.INFO, onClick = {
                        state = 2
                        setting.setLogConfigLevel(AbstractLoggerController.LogPriority.INFO)
                    })
                    Tab(setting.logConfigLevel == AbstractLoggerController.LogPriority.WARNING, onClick = {
                        state = 3
                        setting.setLogConfigLevel(AbstractLoggerController.LogPriority.WARNING)
                    })
                    Tab(setting.logConfigLevel == AbstractLoggerController.LogPriority.ERROR, onClick = {
                        state = 4
                        setting.setLogConfigLevel(AbstractLoggerController.LogPriority.ERROR)
                    })
                    Tab(setting.logConfigLevel == AbstractLoggerController.LogPriority.NONE, onClick = {
                        state = 5
                        setting.setLogConfigLevel(AbstractLoggerController.LogPriority.NONE)
                    })
                }
            }

//            setting.data.forEach {
//                EditView(it, {}, {})
//            }
//            setting.config.forEach {
//                EditView(it, {}, {})
//            }
        }

        VerticalScrollbar(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            adapter = ScrollbarAdapter(scrollState)
        )
    }
}

inline fun <reified T> EnumTabRow() {
    assert(T::class.java.isEnum) { "Not enum class!" }
}

fun AbstractLoggerController.LogPriority.all() {

}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ColorSetSlider(text: String, value: StringColor, onValueChange: (StringColor) -> Unit) {
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
                Text("#$value")
            }
        }
//        TODO:
//        AnimatedVisibility(isExpand) {
//            ColorPicker(value) { red, green, blue, alpha ->
//                onValueChange(Color(red, green, blue, alpha))
//            }
//        }
    }
}

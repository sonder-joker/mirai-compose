package com.youngerhousea.miraicompose.app.ui.setting

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.core.component.setting.*
import net.mamoe.mirai.console.internal.data.builtins.AutoLoginConfig
import net.mamoe.mirai.console.logging.AbstractLoggerController

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
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
            LogColorSettingUi(setting.logColorSetting)
            LoggerLevelSettingUi(setting.logLevelSetting)
            AutoLoginSettingConfig(setting.autoLoginSetting)
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
fun LogColorSettingUi(setting: LogColorSetting) {
    val (debug, setDebug) = remember { mutableStateOf(setting.debug) }
    val (verbose, setVerbose) = remember { mutableStateOf(setting.verbose) }
    val (info, setInfo) = remember { mutableStateOf(setting.info) }
    val (warning, setWarning) = remember { mutableStateOf(setting.warning) }
    val (error, setError) = remember { mutableStateOf(setting.error) }

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

}

@Composable
fun LoggerLevelSettingUi(setting: LogLevelSetting) {
    Row {
        Text("Log level", Modifier.weight(4f))

        TabRow(setting.logConfigLevel.indexFor(), Modifier.weight(1f)) {
            Tab(setting.logConfigLevel == AbstractLoggerController.LogPriority.ALL, onClick = {
                setting.setLogConfigLevel(AbstractLoggerController.LogPriority.ALL)
            })
            Tab(setting.logConfigLevel == AbstractLoggerController.LogPriority.VERBOSE, onClick = {
                setting.setLogConfigLevel(AbstractLoggerController.LogPriority.VERBOSE)
            })

            Tab(setting.logConfigLevel == AbstractLoggerController.LogPriority.DEBUG, onClick = {
                setting.setLogConfigLevel(AbstractLoggerController.LogPriority.DEBUG)
            })
            Tab(setting.logConfigLevel == AbstractLoggerController.LogPriority.INFO, onClick = {
                setting.setLogConfigLevel(AbstractLoggerController.LogPriority.INFO)
            })
            Tab(setting.logConfigLevel == AbstractLoggerController.LogPriority.WARNING, onClick = {
                setting.setLogConfigLevel(AbstractLoggerController.LogPriority.WARNING)
            })
            Tab(setting.logConfigLevel == AbstractLoggerController.LogPriority.ERROR, onClick = {
                setting.setLogConfigLevel(AbstractLoggerController.LogPriority.ERROR)
            })
            Tab(setting.logConfigLevel == AbstractLoggerController.LogPriority.NONE, onClick = {
                setting.setLogConfigLevel(AbstractLoggerController.LogPriority.NONE)
            })
        }
    }
}


inline fun <reified T : Enum<T>> T.indexFor(): Int {
    enumValues<T>().forEachIndexed { index, t ->
        if (this == t)
            return index
    }
    error("Should not happened")
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

@Composable
fun AutoLoginSettingConfig(autoLoginSetting: AutoLoginSetting) {
    val model by autoLoginSetting.model.collectAsState()
    Column {
        model.accountList.forEach {
            Row {
                Text(it.account)
                Text(it.password.value)

                TabRow(it.password.kind.indexFor(), Modifier.weight(1f)) {
                    Tab(it.password.kind == AutoLoginConfig.Account.PasswordKind.PLAIN, onClick = {
                        it.password.kind
                    })

                }
            }
        }
    }
}

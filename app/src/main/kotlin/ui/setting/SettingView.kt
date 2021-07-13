package com.youngerhousea.miraicompose.app.ui.setting

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.app.ui.shared.ColorPicker
import com.youngerhousea.miraicompose.app.utils.ColorSerializer
import com.youngerhousea.miraicompose.app.utils.EnumTabRowWithContent
import com.youngerhousea.miraicompose.core.component.setting.AutoLoginSetting
import com.youngerhousea.miraicompose.core.component.setting.LogColorSetting
import com.youngerhousea.miraicompose.core.component.setting.LogLevelSetting
import com.youngerhousea.miraicompose.core.component.setting.Setting
import com.youngerhousea.miraicompose.core.data.LoginCredential
import net.mamoe.yamlkt.Yaml

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
    val logColor by setting.logColor.collectAsState()

    val debug by derivedStateOf { Yaml.decodeFromString(ColorSerializer, logColor.debug) }

    val verbose by derivedStateOf { Yaml.decodeFromString(ColorSerializer, logColor.verbose) }

    val info by derivedStateOf { Yaml.decodeFromString(ColorSerializer, logColor.info) }

    val warning by derivedStateOf { Yaml.decodeFromString(ColorSerializer, logColor.warning) }

    val error by derivedStateOf { Yaml.decodeFromString(ColorSerializer, logColor.error) }

    Column {
        Row(Modifier.fillMaxWidth()) {
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


@Composable
fun LoggerLevelSettingUi(setting: LogLevelSetting) {
    Column {
        Text("Log level", Modifier.weight(4f))

        val node by setting.node.collectAsState()

//        EnumTabRowWithContent(node,
//            rowModifier = Modifier.width(400.dp),
//            onClick = {
//                setting.setLogConfigLevel(it)
//            }) {
//            Text(it.name)
//
//        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ColorSetSlider(text: String, color: Color, onValueChange: (Color) -> Unit) {
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
private inline fun AutoLoginPage(
    loginCredential: LoginCredential,
    crossinline onSubmit: (loginCredential: LoginCredential) -> Unit
) {
    with(loginCredential) {
        Row {
            TextField(account, onValueChange = {
                onSubmit(loginCredential.copy(account = it))
            })

            TextField(password, onValueChange = {
                onSubmit(loginCredential.copy(password = it))
            })

            EnumTabRowWithContent(passwordKind, onClick = {
                onSubmit(loginCredential.copy(passwordKind = it))
            }) {
                Text(it.name)
            }

            EnumTabRowWithContent(protocolKind, onClick = {
                onSubmit(loginCredential.copy(protocolKind = it))
            }) {
                Text(it.name)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AutoLoginSettingConfig(autoLoginSetting: AutoLoginSetting) {
    val accounts by autoLoginSetting.model.collectAsState()

    Column {
        val (newAutoLoginCredential, setAutoLoginCredential) = remember { mutableStateOf(LoginCredential()) }
        var isExpand by remember { mutableStateOf(false) }
        Button({ isExpand = !isExpand }) {
            Text("Create a auto login account")
        }

        AnimatedVisibility(isExpand) {
            AutoLoginPage(newAutoLoginCredential, onSubmit = setAutoLoginCredential)

            Button({
                autoLoginSetting.addAutoLogin(newAutoLoginCredential)
            }) {
                Text("Add it")
            }
        }

        Text("Now Accounts")

        AnimatedVisibility(accounts.isEmpty()) {
            Text("Not have Auto Login Account")
        }

        AnimatedVisibility(accounts.isNotEmpty()) {
            accounts.forEachIndexed { index, loginCredential ->
                AutoLoginPage(loginCredential) {
                    autoLoginSetting.updateLoginCredential(index, loginCredential)
                }
            }
        }
    }
}

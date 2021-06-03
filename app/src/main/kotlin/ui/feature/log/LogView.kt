package com.youngerhousea.miraicompose.ui.feature.log

import androidx.compose.animation.animateContentSize
import androidx.compose.desktop.AppManager
import androidx.compose.desktop.AppWindow
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.plus
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.console.ComposeLog
import com.youngerhousea.miraicompose.ui.common.CommandSendBox
import com.youngerhousea.miraicompose.ui.common.LogBox
import net.mamoe.mirai.utils.MiraiLogger


/**
 * Compose的所有日志
 *
 */
class ConsoleLog(
    componentContext: ComponentContext,
    val loggerStorage: List<ComposeLog>,
    val logger: MiraiLogger
) : ComponentContext by componentContext

@Composable
fun ConsoleLogUi(consoleLog: ConsoleLog) {
    var isShowSearch by remember { mutableStateOf(true) }

    val (searchText, setSearchText) = remember { mutableStateOf("") }

    Scaffold(topBar = {
        if (isShowSearch)
            TextField(
                searchText,
                setSearchText,
                leadingIcon = { Icon(Icons.Default.Search, null) },
                modifier = Modifier.fillMaxWidth().padding(30.dp).animateContentSize(),
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
    }, floatingActionButton = {
//        FloatingActionButton(onClick = {
//
//        }) {
//        }
    }) {
        Column {
            LogBox(
                Modifier
                    .fillMaxSize()
                    .weight(8f)
                    .padding(horizontal = 40.dp, vertical = 20.dp),
                consoleLog.loggerStorage,
                searchText
            )
            CommandSendBox(
                consoleLog.logger,
                Modifier
                    .weight(1f)
                    .padding(horizontal = 40.dp),
            )
        }
    }


    DisposableEffect(Unit) {
        AppManager.windows.first().let {
            (it as AppWindow).keyboard.setShortcut(Key.CtrlLeft + Key.F) {
                isShowSearch = !isShowSearch
            }
        }
        onDispose {
            AppManager.windows.first().let {
                (it as AppWindow).keyboard.removeShortcut(Key.CtrlLeft + Key.F)
            }
        }
    }
}

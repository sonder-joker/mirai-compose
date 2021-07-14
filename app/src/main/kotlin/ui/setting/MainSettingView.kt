package com.youngerhousea.miraicompose.app.ui.setting

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.app.ui.shared.annotatedName
import com.youngerhousea.miraicompose.core.component.setting.MainSetting

@Composable
fun MainSettingUi(main: MainSetting) {
    val scrollState = rememberScrollState()

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .verticalScroll(scrollState)
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Row {
                Text("Auto Login")
                Button(main::routeAutoLogin) {

                }
            }

            Row {
                Text("Log Color")
                Button(main::routeLoginColor) {

                }
            }
            Row {
                Text("Log Level")
                Button(main::routeLoginLevel) {

                }
            }

            PluginControlSettingUi(main.pluginControlSetting)
        }

        VerticalScrollbar(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            adapter = ScrollbarAdapter(scrollState)
        )
    }
}
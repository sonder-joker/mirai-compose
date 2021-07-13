package com.youngerhousea.miraicompose.app.ui.setting

import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        }

        VerticalScrollbar(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            adapter = ScrollbarAdapter(scrollState)
        )
    }
}
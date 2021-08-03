package com.youngerhousea.miraicompose.app.ui.setting

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.youngerhousea.miraicompose.core.component.setting.MainSetting

@Composable
fun MainSettingUi(main: MainSetting) {
    val scrollState = rememberScrollState()

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .verticalScroll(scrollState)
                .padding(20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SettingRow(
                describe = { Text("Auto Login") },
                leadingIcon = {
                    Icon(
                        Icons.Default.ArrowRight,
                        null,
                        Modifier.clickable(onClick = main::routeAutoLogin)
                    )
                }
            )

            SettingRow(
                describe = { Text("Log Color") },
                leadingIcon = {
                    Icon(
                        Icons.Default.ArrowRight,
                        null,
                        Modifier.clickable(onClick = main::routeLoginColor)
                    )
                }
            )

            SettingRow(
                describe = { Text("Log Level") },
                leadingIcon = {
                    Icon(
                        Icons.Default.ArrowRight,
                        null,
                        Modifier.clickable(onClick = main::routeLoginLevel)
                    )
                }
            )

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

@Composable
private fun SettingRow(
    describe: @Composable () -> Unit,
    leadingIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier.fillMaxWidth().padding(SettingRowPadding).background(color = Color.LightGray).shadow(1.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        describe()
        leadingIcon()
    }
}

private val SettingRowPadding = 10.dp

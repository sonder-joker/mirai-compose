package com.youngerhousea.miraicompose.app.ui.plugin

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.youngerhousea.miraicompose.app.ui.plugin.shared.JvmPluginUi
import com.youngerhousea.miraicompose.core.component.plugin.CKotlinPlugin


@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun CKotlinPluginUi(cKotlinPlugin: CKotlinPlugin) {
    JvmPluginUi(
        cKotlinPlugin.state,
        cKotlinPlugin::onDescriptionClick,
        cKotlinPlugin::onDataClick,
        cKotlinPlugin::onCommandClick
    )
}

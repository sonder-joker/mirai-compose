package com.youngerhousea.miraicompose.app.ui.plugin

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.youngerhousea.miraicompose.app.ui.plugin.shared.JvmPluginUi
import com.youngerhousea.miraicompose.core.component.plugin.CJavaPlugin

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun CJavaPluginUi(cJavaPlugin: CJavaPlugin) {
    JvmPluginUi(
        cJavaPlugin.state,
        cJavaPlugin::onDescriptionClick,
        cJavaPlugin::onDataClick,
        cJavaPlugin::onCommandClick
    )
}
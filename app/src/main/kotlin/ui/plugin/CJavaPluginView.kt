package com.youngerhousea.miraicompose.app.ui.plugin

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.youngerhousea.miraicompose.app.ui.plugin.shared.JvmPluginUi
import com.youngerhousea.miraicompose.core.component.plugin.CJavaPlugin
import com.youngerhousea.miraicompose.core.component.plugin.shared.DetailedCommand
import com.youngerhousea.miraicompose.core.component.plugin.shared.DetailedData
import com.youngerhousea.miraicompose.core.component.plugin.shared.DetailedDescription

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun CJavaPluginUi(cJavaPlugin: CJavaPlugin) {
    JvmPluginUi(
        cJavaPlugin::onDescriptionClick,
        cJavaPlugin::onDataClick,
        cJavaPlugin::onCommandClick
    ) {
        Children(cJavaPlugin.state, crossfade()) { child ->
            when (val ch = child.instance) {
                is CJavaPlugin.Children.Command -> DetailedCommandUi(ch.detailedCommand)
                is CJavaPlugin.Children.Data -> DetailedDataUi(ch.detailedData)
                is CJavaPlugin.Children.Description -> DetailedDescriptionUi(ch.detailedDescription)
            }
        }
    }
}
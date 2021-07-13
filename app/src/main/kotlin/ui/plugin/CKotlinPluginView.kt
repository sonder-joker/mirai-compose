package com.youngerhousea.miraicompose.app.ui.plugin

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.youngerhousea.miraicompose.app.ui.plugin.shared.JvmPluginUi
import com.youngerhousea.miraicompose.core.component.plugin.CKotlinPlugin


@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun CKotlinPluginUi(cKotlinPlugin: CKotlinPlugin) {
    JvmPluginUi(
        cKotlinPlugin::onDescriptionClick,
        cKotlinPlugin::onDataClick,
        cKotlinPlugin::onCommandClick
    ) {
        Children(cKotlinPlugin.state, crossfade()) { child ->
            when (val ch = child.instance) {
                is CKotlinPlugin.Children.Command -> DetailedCommandUi(ch.detailedCommand)
                is CKotlinPlugin.Children.Data -> DetailedDataUi(ch.detailedData)
                is CKotlinPlugin.Children.Description -> DetailedDescriptionUi(ch.detailedDescription)
            }
        }
    }
}

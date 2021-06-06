package com.youngerhousea.miraicompose.app.ui.plugin

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.youngerhousea.miraicompose.core.component.plugin.CommonPlugin
import com.youngerhousea.miraicompose.core.component.plugin.shared.DetailedDescription


@Composable
fun CommonPluginUi(commonPlugin: CommonPlugin) {
    Children(commonPlugin.state) { child ->
        when(val ch = child.instance) {
            is DetailedDescription -> DetailedDescriptionUi(ch)
        }
    }
}

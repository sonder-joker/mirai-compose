package com.youngerhousea.miraicompose.ui.plugin

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.youngerhousea.miraicompose.component.plugin.CommonPlugin
import com.youngerhousea.miraicompose.component.plugin.shared.DetailedDescription


@Composable
fun CommonPluginUi(commonPlugin: CommonPlugin) {
    Children(commonPlugin.state) { child ->
        when(val ch = child.instance) {
            is DetailedDescription -> DetailedDescriptionUi(ch)
        }
    }
}

package com.youngerhousea.miraicompose.app.ui.plugin

import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.youngerhousea.miraicompose.core.component.plugin.CJavaPlugin
import com.youngerhousea.miraicompose.core.component.plugin.CKotlinPlugin
import com.youngerhousea.miraicompose.core.component.plugin.CommonPlugin
import com.youngerhousea.miraicompose.core.component.plugin.SpecificPlugin
import com.youngerhousea.miraicompose.app.ui.shared.annotatedName

@Composable
fun SpecificPluginUi(specificPlugin: SpecificPlugin) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                specificPlugin.plugin.annotatedName,
                textAlign = TextAlign.Center,
                maxLines = 1,
            )
        }, navigationIcon = {
            Icon(
                Icons.Default.KeyboardArrowLeft,
                null,
                Modifier.clickable(onClick = specificPlugin.onExitButtonClicked)
            )
        })
    }) {
        Children(specificPlugin.state) { child ->
            when(val ch = child.instance) {
                is CommonPlugin -> CommonPluginUi(ch)
                is CJavaPlugin -> CJavaPluginUi(ch)
                is CKotlinPlugin -> CKotlinPluginUi(ch)
            }
        }
    }
}

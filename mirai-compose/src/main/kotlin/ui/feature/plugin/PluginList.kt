package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.ui.common.PluginDescriptionCard
import net.mamoe.mirai.console.plugin.Plugin

class PluginList(
    componentContext: ComponentContext,
    val plugins: List<Plugin>,
    val onItemSelected: (plugin: Plugin) -> Unit
) : ComponentContext by componentContext {


}

@Composable
fun PluginListUi(pluginList: PluginList) {

    LazyVerticalGrid(
        cells = GridCells.Adaptive(300.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp)
    ) {
        items(pluginList.plugins) { plugin ->
            PluginDescriptionCard(plugin, onItemSelected = pluginList.onItemSelected)
        }
    }
}
package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.ui.common.PluginDescription
import net.mamoe.mirai.console.plugin.Plugin

class PluginList(
    componentContext: ComponentContext,
    val plugins: List<Plugin>,
    val onPluginCardSelected: (plugin: Plugin) -> Unit
) : ComponentContext by componentContext

@Composable
fun PluginListUi(pluginList: PluginList) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(300.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp)
    ) {
        items(pluginList.plugins) { plugin ->
            Card(
                Modifier
                    .padding(10.dp)
                    .clickable(onClick = { pluginList.onPluginCardSelected(plugin) })
                    .requiredHeight(150.dp)
                    .fillMaxWidth()
            ) {
                PluginDescription(plugin, Modifier.padding(10.dp))
            }
        }
    }
}
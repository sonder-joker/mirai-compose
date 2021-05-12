package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.future.inject
import com.youngerhousea.miraicompose.ui.common.PluginDescription
import net.mamoe.mirai.console.plugin.Plugin
import org.koin.core.qualifier.named

/**
 * 插件列表
 *
 */
class PluginList(
    componentContext: ComponentContext,
    val onPluginCardClick: (plugin: Plugin) -> Unit
) : ComponentContext by componentContext {
    val plugins: List<Plugin> by inject(named("Plugin"))
}

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
                    .clickable(onClick = { pluginList.onPluginCardClick(plugin) })
                    .requiredHeight(150.dp)
                    .fillMaxWidth(),
                backgroundColor = Color(0xff979595),
                contentColor = Color(0xffffffff)
            ) {
                PluginDescription(plugin, Modifier.padding(10.dp))
            }
        }
    }
}
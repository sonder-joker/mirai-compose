package com.youngerhousea.miraicompose.ui.plugin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youngerhousea.miraicompose.console.getPluginConfig
import com.youngerhousea.miraicompose.console.getPluginData
import com.youngerhousea.miraicompose.model.PluginState
import net.mamoe.mirai.console.plugin.*


@Composable
fun PluginsWindow(pluginState: PluginState) {

    if (pluginState.isSingleCard) {
        DetailedPluginCard(
            PluginManager.plugins[pluginState.currentIndex]
        ) {
            pluginState.isSingleCard = false
        }
    } else {
        LazyVerticalGrid(
            cells = GridCells.Adaptive(400.dp),
            Modifier.fillMaxSize(),
        ) {
            itemsIndexed(PluginManager.plugins) { index, plugin ->
                PluginCard(
                    Modifier
                        .padding(40.dp)
                        .clickable {
                            pluginState.currentIndex = index
                            pluginState.isSingleCard = true
                        }
                        .requiredHeight(200.dp)
                        .fillMaxWidth(),
                    plugin
                )
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
private fun PluginCard(modifier: Modifier, plugin: Plugin) {
    Card(modifier) {
        Column(
            Modifier
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Text(plugin.name, fontWeight = FontWeight.Bold, fontSize = 30.sp)
            Text("author: ${plugin.author}", fontSize = 15.sp)
            Text("info: ${plugin.info}", fontSize = 20.sp)
        }
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
private fun DetailedPluginCard(plugin: Plugin, onExit: () -> Unit) {
    Card {
        Column {
            Icon(
                Icons.Default.KeyboardArrowLeft, null,
                Modifier.clickable(onClick = onExit)
            )
            Text(plugin.name, Modifier.padding(start = 20.dp), fontWeight = FontWeight.Bold, fontSize = 30.sp)
            Text("author: ${plugin.author}", Modifier.padding(start = 20.dp), fontSize = 20.sp)
            Text("info: ${plugin.info}", Modifier.padding(start = 20.dp), fontSize = 20.sp)
            PluginSettingView(plugin.getPluginConfig() + plugin.getPluginData())
        }
    }

}


package com.youngerhousea.miraicompose.ui.plugin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.console.getPluginConfig
import com.youngerhousea.miraicompose.console.getPluginData
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.author
import net.mamoe.mirai.console.plugin.info
import net.mamoe.mirai.console.plugin.name

typealias Content = @Composable () -> Unit


@Composable
fun PluginsWindow(pluginModel: List<Plugin>) {
    var pluginState by remember { mutableStateOf<PluginView>(PluginView.List) }

    when (val current = pluginState) {
        is PluginView.Details -> {
            DetailedPluginCard(
                current.plugin,
                onExit = {
                    pluginState = PluginView.List
                }
            )
        }

        is PluginView.List -> {
            PluginCardList(pluginModel) {
                pluginState = PluginView.Details(it)
            }

        }
    }
}

@Composable
private fun PluginCardList(plugins: List<Plugin>, onClick: (Plugin) -> Unit) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(400.dp),
        Modifier.fillMaxSize(),
    ) {
        items(plugins) { plugin ->
            PluginCard(
                plugin,
                onClick = { onClick(plugin) }
            )
        }
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
private fun PluginCard(plugin: Plugin, onClick: () -> Unit) {
    Card(
        Modifier
            .padding(40.dp)
            .clickable(onClick = onClick)
            .requiredHeight(200.dp)
            .fillMaxWidth()
    ) {
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
internal fun DetailedPluginCard(plugin: Plugin, onExit: () -> Unit) {
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

private sealed class PluginView : Parcelable {
    object List : PluginView()
    class Details(val plugin: Plugin) : PluginView()
}


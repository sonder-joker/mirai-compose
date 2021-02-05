package com.youngerhousea.miraicompose.ui.plugin

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youngerhousea.miraicompose.console.getPluginData
import net.mamoe.mirai.console.data.AbstractPluginData
import net.mamoe.mirai.console.plugin.*


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PluginsWindow(modifier: Modifier) = LazyVerticalGrid(
    cells = GridCells.Adaptive(400.dp),
    modifier,
    contentPadding = PaddingValues(20.dp)
) {
    items(PluginManager.plugins) {
        PluginCard(
            Modifier
                .padding(20.dp)
                .preferredHeight(200.dp)
                .fillMaxWidth(), it
        )
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
            Icon(Icons.Default.Settings, "Ban")
            PluginSettingView(plugin.getPluginData() as MutableList<AbstractPluginData>)
        }
    }
}


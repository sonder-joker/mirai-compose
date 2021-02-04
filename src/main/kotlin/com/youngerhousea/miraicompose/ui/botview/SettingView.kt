package com.youngerhousea.miraicompose.ui.botview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import net.mamoe.mirai.console.plugin.*


@Composable
fun SettingWindow(modifier: Modifier) = LazyColumn(
    modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
) {
    item {
        Text(
            text = "插件列表",
            modifier = Modifier
                .preferredHeight(40.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }

    items(PluginManager.plugins) {
        PluginColumn(
            Modifier
                .preferredHeight(40.dp)
                .fillMaxWidth(), it
        )
    }
}

@Composable
private fun PluginColumn(modifier: Modifier, plugin: Plugin) {
    Row(
        modifier,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(plugin.id)
        Text(plugin.name)
        Text(plugin.author)
        Text(plugin.info)
        Text(plugin.version.toString())
        Text("无依赖")
        Icon(Icons.Default.Settings, "设置")
    }
}
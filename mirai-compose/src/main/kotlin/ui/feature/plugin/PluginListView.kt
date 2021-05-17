package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.theme.R
import com.youngerhousea.miraicompose.ui.common.PluginDescription
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.plugin.Plugin
import java.awt.Desktop
import kotlin.io.path.div

/**
 * 插件列表
 *
 */
class PluginList(
    componentContext: ComponentContext,
    val onPluginCardClick: (plugin: Plugin) -> Unit
) : ComponentContext by componentContext {
    val plugins: List<Plugin> = MiraiCompose.loadedPlugins
}

@Composable
fun PluginListUi(pluginList: PluginList) {
    Box(Modifier.clipToBounds()) {
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
        Button(
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = {
                Desktop.getDesktop().open((MiraiCompose.rootPath / "plugins").toFile())
            }) {
            Text(R.String.addPlugin)
        }
    }
}
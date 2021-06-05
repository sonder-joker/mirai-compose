package com.youngerhousea.miraicompose.ui.plugin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.component.plugin.PluginList
import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.theme.R
import com.youngerhousea.miraicompose.ui.shared.PluginDescription
import java.awt.Desktop
import kotlin.io.path.div


@Composable
fun PluginListUi(pluginList: PluginList) {
    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = pluginList.snackbarHostState),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(50.dp),
                backgroundColor = Color(0xff6EC177),
                contentColor = Color.White,
                onClick = {
                    Desktop.getDesktop().open((MiraiCompose.rootPath / "plugins").toFile())
                },
                shape = MaterialTheme.shapes.medium.copy(CornerSize(percent = 50))
            ) {
                Icon(Icons.Filled.Add, R.String.addPlugin)
            }
        }
    ) {
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
}

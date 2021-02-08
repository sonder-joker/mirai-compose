package com.youngerhousea.miraicompose.ui.plugin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youngerhousea.miraicompose.console.getPluginData
import net.mamoe.mirai.console.data.AbstractPluginData
import net.mamoe.mirai.console.plugin.*


@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun PluginsWindow(modifier: Modifier) {
    var isSingleCard by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableStateOf(0) }

    AnimatedVisibility(
        isSingleCard,
    ) {
        DetailedPluginCard(Modifier.fillMaxSize(), PluginManager.plugins[currentIndex]) {
            isSingleCard = false
        }
    }
    AnimatedVisibility(
        !isSingleCard,
    ) {
        LazyVerticalGrid(
            cells = GridCells.Adaptive(400.dp),
            modifier,
            contentPadding = PaddingValues(20.dp)
        ) {
            itemsIndexed(PluginManager.plugins) { index, plugin ->
                PluginCard(
                    Modifier
                        .padding(20.dp)
                        .clickable {
                            currentIndex = index
                            isSingleCard = true
                        }
                        .preferredHeight(200.dp)
                        .fillMaxWidth(),
                    plugin
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
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
private fun DetailedPluginCard(modifier: Modifier, plugin: Plugin, onClick: () -> Unit) {
    Card(modifier) {
        Column {
            Icon(
                Icons.Default.KeyboardArrowLeft, null,
                Modifier.clickable(onClick = onClick)
            )
            Text(plugin.name, fontWeight = FontWeight.Bold, fontSize = 30.sp)
            Text("author: ${plugin.author}", fontSize = 15.sp)
            Text("info: ${plugin.info}", fontSize = 20.sp)
            Spacer(Modifier.fillMaxWidth().height(10.dp).background(color = Color.Yellow))
            PluginSettingView(plugin.getPluginData() as MutableList<AbstractPluginData>)
        }
    }
}



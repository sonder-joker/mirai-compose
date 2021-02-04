package com.youngerhousea.miraicompose.ui.botview

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import net.mamoe.mirai.console.plugin.*

private val itemHeight = 50.dp
private val itemWidth = 130.dp

@Composable
fun SettingWindow(modifier: Modifier) = LazyColumn(
    modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
) {
//    var inSetting by mutableStateOf(false)
    item {
        SplitText("插件列表")
    }
    item {
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SplitText("ID")
            SplitText("Name")
            SplitText("Author")
            SplitText("Info")
            SplitText("Version")
            SplitText("Dependencies")
            SplitText("Setting")
        }
    }

    items(PluginManager.plugins) {

        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SplitText(it.id)
            SplitText(it.name)
            SplitText(it.author)
            SplitText(it.info)
            SplitText(it.version.toString())
            if (it.dependencies.isNotEmpty())
                SplitText(it.dependencies.joinToString { "," })
            else
                SplitText("无依赖")

            Box(
                modifier = Modifier
                    .height(itemHeight)
                    .width(itemWidth)
            ) {
                Icon(
                    Icons.Default.Settings,
                    "Setting",
                    modifier = Modifier
                        .padding(end = 70.dp)
                        .clickable {
                        }
                        .matchParentSize()
                )
            }
        }
    }
}

@Composable
private fun SplitText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier
            .width(itemWidth)
            .height(itemHeight)
    )
}
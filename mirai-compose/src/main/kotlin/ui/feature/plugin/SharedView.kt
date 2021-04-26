package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.ui.common.EditView
import com.youngerhousea.miraicompose.ui.common.annotatedDescription
import com.youngerhousea.miraicompose.ui.common.annotatedExplain
import com.youngerhousea.miraicompose.ui.common.simpleDescription
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.plugin.Plugin

/**
 * 插件描述
 */
class DetailedDescription(
    componentContext: ComponentContext,
    val plugin: Plugin
) : ComponentContext by componentContext

@Composable
fun DetailedDescriptionUi(detailedDescription: DetailedDescription) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(detailedDescription.plugin.annotatedDescription)
    }
}

/**
 * 插件数据
 * Only for jvm
 */
class DetailedData(
    componentContext: ComponentContext,
    val data: List<PluginData>,
) : ComponentContext by componentContext

@Composable
fun DetailedDataUi(detailedData: DetailedData) {
    LazyColumn(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(detailedData.data) { pluginData ->
            Text(pluginData.annotatedExplain, Modifier.padding(bottom = 40.dp))
            EditView(pluginData)
        }
    }
}

/**
 *  插件指令
 */
class DetailedCommand(
    componentContext: ComponentContext,
    val command: List<Command>,
) : ComponentContext by componentContext

@Composable
fun DetailedCommandUi(detailedCommand: DetailedCommand) {
    LazyColumn {
        items(detailedCommand.command) { registeredCommand ->
            Text(registeredCommand.simpleDescription)
            Spacer(Modifier.height(20.dp))
        }
    }
}
package com.youngerhousea.miraicompose.app.ui.plugin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.core.component.plugin.shared.DetailedCommand
import com.youngerhousea.miraicompose.core.component.plugin.shared.DetailedData
import com.youngerhousea.miraicompose.core.component.plugin.shared.DetailedDescription
import com.youngerhousea.miraicompose.app.ui.shared.annotatedDescription
import com.youngerhousea.miraicompose.app.ui.shared.annotatedExplain
import com.youngerhousea.miraicompose.app.ui.shared.simpleDescription
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.yamlkt.Yaml


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

@Composable
private fun EditView(pluginData: PluginData) {
    var value by remember(pluginData) {
        mutableStateOf(
            Yaml.encodeToString(
                pluginData.updaterSerializer,
                Unit
            )
        )
    }

    var textField by remember(pluginData) { mutableStateOf(value) }

    Row(
        Modifier.fillMaxWidth().padding(bottom = 40.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        OutlinedTextField(textField, {
            textField = it
        })
        Button(
            {
                runCatching {
                    Yaml.decodeFromString(pluginData.updaterSerializer, textField)
                }.onSuccess {
                    value = textField
                }
            },
            Modifier
                .requiredWidth(100.dp)
                .background(MaterialTheme.colors.background)
        ) {
            Text("Change")
        }
    }
}


@Composable
fun DetailedCommandUi(detailedCommand: DetailedCommand) {
    LazyColumn {
        items(detailedCommand.commands) { registeredCommand ->
            Column {
                Text(registeredCommand.simpleDescription)
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}


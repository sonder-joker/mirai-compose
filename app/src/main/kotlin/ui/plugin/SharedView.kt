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
import com.youngerhousea.miraicompose.app.ui.shared.simpleDescription
import com.youngerhousea.miraicompose.app.utils.R
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.CommandExecuteResult
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.command.executeCommand
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.yamlkt.Yaml
import kotlin.reflect.KProperty

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
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
        LazyColumn(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(detailedData.data) { pluginData ->
                EditView(pluginData,
                    onEditSuccess = {
                        scope.launch {
                            snackbarHostState.showSnackbar(R.String.editSuccess)
                        }
                    },
                    onEditFailure = {
                        scope.launch {
                            snackbarHostState.showSnackbar(R.String.editFailure)
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun EditView(pluginData: PluginData, onEditSuccess: () -> Unit, onEditFailure: (Throwable) -> Unit) {
    var obValue by pluginData

    var textField by remember(pluginData) { mutableStateOf(obValue) }

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
                    obValue = textField
                }.onSuccess {
                    onEditSuccess()
                }.onFailure {
                    textField = obValue
                    onEditFailure(it)
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


operator fun PluginData.getValue(thisRef: Any?, property: KProperty<*>): String =
    Yaml.encodeToString(updaterSerializer, Unit)

operator fun PluginData.setValue(thisRef: Any?, property: KProperty<*>, value: String) =
    Yaml.decodeFromString(updaterSerializer, value)


@OptIn(ExperimentalCommandDescriptors::class)
@Composable
fun DetailedCommandUi(detailedCommand: DetailedCommand) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
        LazyColumn {
            items(detailedCommand.commands) { registeredCommand ->
                Column {
                    var commandValue by remember { mutableStateOf("") }
                    Text(registeredCommand.simpleDescription)
                    TextField(commandValue, {
                        commandValue = it
                    })
                    Button({
//                        scope.launch {
//                            val result = ConsoleCommandSender.executeCommand(commandValue)
//                            when (result) {
//                                is CommandExecuteResult.Success -> {
//                                }
//                            }
//                        }
                    }) {
                        Text("Quick go")
                    }
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}


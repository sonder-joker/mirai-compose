package com.youngerhousea.mirai.compose.ui.plugins

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.youngerhousea.mirai.compose.console.impl.MiraiCompose
import com.youngerhousea.mirai.compose.console.impl.get
import com.youngerhousea.mirai.compose.resource.R
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.registeredCommands
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.jvm.AbstractJvmPlugin
import net.mamoe.yamlkt.Yaml
import kotlin.reflect.KProperty

@Composable
fun SinglePlugin(
    plugin: Plugin,
    onExit: () -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                plugin.annotatedName,
                textAlign = TextAlign.Center,
                maxLines = 1,
            )
        }, navigationIcon = {
            Icon(
                R.Icon.Back,
                null,
                Modifier.clickable(onClick = onExit)
            )
        })
    }) {
        JvmPlugin(plugin as AbstractJvmPlugin)
    }
}

enum class PluginTab {
    Description,
    Data,
    Command
}

@Composable
fun JvmPlugin(
    plugin: AbstractJvmPlugin,
) {
    Column {
        val (pluginTab, setPluginTab) = rememberSaveable(plugin) { mutableStateOf(PluginTab.Data) }

        TabRow(pluginTab.ordinal) {
            for (current in enumValues<PluginTab>()) {
                Tab(pluginTab == current, onClick = {
                    setPluginTab(current)
                }, content = {
                    Text(current.name)
                })
            }
        }

        when (pluginTab) {
            PluginTab.Description -> PluginDescription(plugin)
            PluginTab.Data -> PluginDataList(MiraiCompose.configStorageForBuiltIns[plugin])
            PluginTab.Command -> PluginCommands(plugin.registeredCommands)
        }
    }
}

@Composable
fun PluginDescription(plugin:Plugin) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(plugin.annotatedDescription)
    }
}

@Composable
fun PluginDataList(pluginDataList: List<PluginData>) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
        LazyColumn(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(pluginDataList) { pluginData ->
                EditView(pluginData,
                    onEditSuccess = {
                        scope.launch {
                            snackbarHostState.showSnackbar(R.String.EditSuccess)
                        }
                    },
                    onEditFailure = {
                        scope.launch {
                            snackbarHostState.showSnackbar(R.String.EditFailure)
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


@Composable
fun PluginCommands(commands: List<Command>) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
        LazyColumn {
            items(commands) { registeredCommand ->
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
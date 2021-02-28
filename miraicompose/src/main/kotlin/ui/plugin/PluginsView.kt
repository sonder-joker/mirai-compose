package com.youngerhousea.miraicompose.ui.plugin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.arkivanov.decompose.pop
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.console.getPluginConfig
import com.youngerhousea.miraicompose.console.getPluginData
import com.youngerhousea.miraicompose.theme.AppTheme
import com.youngerhousea.miraicompose.utils.mutableStateListOf
import net.mamoe.mirai.console.data.*
import net.mamoe.mirai.console.plugin.*
import net.mamoe.yamlkt.Yaml

interface Content {
    @Composable
    fun render()
}

@Composable
fun PluginsWindow() {
    val a = rememberRootComponent { componentContext ->
        PluginsWindow(plugins = PluginManager.plugins, componentContext)
    }
    a.render()
}


class PluginsWindow(
    val plugins: List<Plugin>,
    componentContext: ComponentContext
) : Content, ComponentContext by componentContext {

    sealed class PluginView : Parcelable {
        object List : PluginView()
        class Details(val plugin: Plugin) : PluginView()
    }

    private val router = router<PluginView, Content>(
        initialConfiguration = PluginView.List,
        componentFactory = ::createChild
    )

    init {
        stateKeeper.register("state") {
            PluginView.List
        }
    }

    val routerState get() = router.state

    private fun createChild(pluginView: PluginView, context: ComponentContext): Content =
        when (pluginView) {
            is PluginView.List -> {
                PluginCardList(context,
                    plugins,
                    onItemSelected = { router.push(PluginView.Details(it)) }
                )
            }
            is PluginView.Details -> {
                DetailedPluginCard(context,
                    pluginView.plugin,
                    onExit = { router.pop() })
            }
        }

    @Composable
    override fun render() {
        Children(routerState) { child: Content, _: PluginView ->
            child.render()
        }
    }
}


class PluginCardList(
    context: ComponentContext,
    plugins: List<Plugin>,
    val onItemSelected: (plugin: Plugin) -> Unit
) : Content, ComponentContext by context {
    val plugins = mutableStateListOf(plugins)

    @Composable
    override fun render() {
        LazyVerticalGrid(
            cells = GridCells.Adaptive(400.dp),
            Modifier.fillMaxSize(),
        ) {
            items(plugins) { plugin ->
                Card(
                    Modifier
                        .padding(40.dp)
                        .clickable(onClick = { onItemSelected(plugin) })
                        .requiredHeight(200.dp)
                        .fillMaxWidth()
                ) {
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
        }
    }
}


class DetailedPluginCard(
    context: ComponentContext,
    plugin: Plugin,
    val onExit: () -> Unit
) : Content, ComponentContext by context {
    val plugin by mutableStateOf(plugin)

    val configAndData get() = plugin.getPluginConfig() + plugin.getPluginData()

    @Suppress("UNCHECKED_CAST")
    @Composable
    override fun render() {
        Card {
            Column {
                Icon(
                    Icons.Default.KeyboardArrowLeft, null,
                    Modifier.clickable(onClick = onExit)
                )
                Text(plugin.name, Modifier.padding(start = 20.dp), fontWeight = FontWeight.Bold, fontSize = 30.sp)
                Text("author: ${plugin.author}", Modifier.padding(start = 20.dp), fontSize = 20.sp)
                Text("info: ${plugin.info}", Modifier.padding(start = 20.dp), fontSize = 20.sp)
                PluginSettingView(configAndData)
            }
        }

    }

}

private val yaml = Yaml.default

@Composable
private fun PluginSettingView(pluginDatas: List<PluginData>) {
    LazyColumn(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(pluginDatas) { pluginData ->
            val kind = remember {
                when (pluginData) {
                    is AutoSavePluginData -> {
                        "自动保存数据"
                    }
                    is AutoSavePluginConfig ->
                        "自动保存配置"
                    is ReadOnlyPluginData ->
                        "只读数据"
                    is ReadOnlyPluginConfig ->
                        "只读配置"
                    else -> "未知类型数据"
                }
            }
            Text(
                "$kind:${pluginData.saveName}", Modifier
                    .padding(bottom = 40.dp),
                fontSize = 20.sp
            )
            var value by remember(pluginData) {
                mutableStateOf(
                    yaml.encodeToString(
                        pluginData.updaterSerializer,
                        Unit
                    )
                )
            }
            var textField by remember(pluginData) { mutableStateOf(TextFieldValue(value)) }

            Row(
                Modifier.fillMaxWidth().padding(bottom = 40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(value, textAlign = TextAlign.Center)
                TextField(textField, {
                    textField = it
                })
                Button(
                    {
                        kotlin.runCatching {
                            yaml.decodeFromString(pluginData.updaterSerializer, textField.text)
                        }.onSuccess {
                            value = textField.text
                        }.onFailure {
                            it.printStackTrace()
                        }
                    },
                    Modifier
                        .requiredWidth(100.dp)
                        .background(AppTheme.Colors.backgroundDark)
                ) {
                    Text("修改")
                }
            }
        }
    }
}

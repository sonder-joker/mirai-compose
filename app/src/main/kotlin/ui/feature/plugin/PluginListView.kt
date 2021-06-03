package com.youngerhousea.miraicompose.ui.feature.plugin

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
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.theme.R
import com.youngerhousea.miraicompose.ui.common.PluginDescription
import com.youngerhousea.miraicompose.utils.componentScope
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

    val scope = componentScope()

    val snackbarHostState = SnackbarHostState()

//    val onAddPluginClick: (File) -> Unit = { file ->
//        scope.launch {
//            when {
//                !file.exists() || !file.isFile -> {
//                    snackbarHostState.showSnackbar("选择的文件(${file.absolutePath})不存在或不是文件")
//                }
//                !file.name.endsWith(".jar") -> {
//                    snackbarHostState.showSnackbar("选择的文件(${file.absolutePath})不是mirai插件(.jar)")
//                }
//                file.canRead() -> {
//                    val target = (MiraiCompose.rootPath / "plugins" / file.name).toFile()
//                    if (target.exists()) {
//                        if (!target.canWrite()) {
//                            snackbarHostState.showSnackbar("导入失败, ${target.absolutePath}已存在并不可更改")
//                        }
//                        snackbarHostState.showSnackbar("${file.name}已存在，将会覆盖旧版本")
//                    }
//                    file.copyTo(target, true)
//                    snackbarHostState.showSnackbar("成功导入${file.name}插件")
//                }
//                else -> {
//                    snackbarHostState.showSnackbar("导入失败, ${file.absolutePath}无法读取")
//                }
//            }
//        }
//    }
}

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

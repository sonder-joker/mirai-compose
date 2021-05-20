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
import com.youngerhousea.miraicompose.utils.FileChooser
import net.mamoe.mirai.console.plugin.Plugin
import java.awt.Desktop
import java.io.File
import javax.swing.filechooser.FileNameExtensionFilter
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
        Row(modifier = Modifier.align(Alignment.BottomEnd)) {
            Button(
                modifier = Modifier.padding(5.dp),
                onClick = {
                    val fc = FileChooser(
                        R.String.addPlugin,
                        FileNameExtensionFilter("Mirai console plugin(*.mirai.jar)", "mirai.jar"),
                        File("example.mirai.jar"),
                        ".",
                        false
                    ) ?: let { return@Button }
                    if (!fc.selectedFile.exists() || !fc.selectedFile.isFile) {
                        MiraiCompose.logger.error("选择的文件(${fc.selectedFile.absolutePath})不存在或不是文件")
                        return@Button
                    }
                    if (!fc.selectedFile.name.endsWith(".mirai.jar")) {
                        MiraiCompose.logger.error("选择的文件(${fc.selectedFile.absolutePath})不是mirai插件(.mirai.jar)")
                        return@Button
                    }
                    val target = (MiraiCompose.rootPath / "plugins" / fc.selectedFile.name).toFile()
                    if (fc.selectedFile.canRead()) {
                        if (target.exists()) {
                            if (!target.canWrite()) {
                                MiraiCompose.logger.error("导入失败, ${target.absolutePath}已存在并不可更改")
                                return@Button
                            }
                            MiraiCompose.logger.warning("${fc.selectedFile.name}已存在，将会覆盖旧版本")
                        }
                        fc.selectedFile.copyTo(target, true)
                        MiraiCompose.logger.info("成功导入${fc.selectedFile.name}插件")
                        //TODO: do something to load plugin
                    } else {
                        MiraiCompose.logger.error("导入失败, ${fc.selectedFile.absolutePath}无法读取")
                    }
                }) {
                Text(R.String.addPlugin)
            }
            Button(
                modifier = Modifier.padding(5.dp),
                onClick = {
                    Desktop.getDesktop().open((MiraiCompose.rootPath / "plugins").toFile())
                }) {
                Text(R.String.openPluginFolder)
            }
        }
    }
}
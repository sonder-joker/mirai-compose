package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.theme.R
import com.youngerhousea.miraicompose.ui.common.PluginDescription
import com.youngerhousea.miraicompose.ui.feature.log.onRightClick
import com.youngerhousea.miraicompose.utils.FileChooser
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.utils.MiraiLogger
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
    logger: MiraiLogger,
    val onPluginCardClick: (plugin: Plugin) -> Unit
) : ComponentContext by componentContext {
    val plugins: List<Plugin> = MiraiCompose.loadedPlugins
    val onAddPluginClick = L@{ f: File ->
        if (!f.exists() || !f.isFile) {
            logger.error("选择的文件(${f.absolutePath})不存在或不是文件")
            return@L
        }
        if (!f.name.endsWith(".mirai.jar")) {
            logger.error("选择的文件(${f.absolutePath})不是mirai插件(.mirai.jar)")
            return@L
        }
        val target = (MiraiCompose.rootPath / "plugins" / f.name).toFile()
        if (f.canRead()) {
            if (target.exists()) {
                if (!target.canWrite()) {
                    logger.error("导入失败, ${target.absolutePath}已存在并不可更改")
                    return@L
                }
                logger.warning("${f.name}已存在，将会覆盖旧版本")
            }
            f.copyTo(target, true)
            logger.info("成功导入${f.name}插件")
            //TODO: do something to load plugin
        } else {
            logger.error("导入失败, ${f.absolutePath}无法读取")
        }
    }
}

@Composable
fun PluginListUi(pluginList: PluginList) {
    var isExpand by remember { mutableStateOf(false) }
    var offset by remember { mutableStateOf(DpOffset(100.dp, 100.dp)) }
    Box(
        modifier = Modifier
            .clipToBounds()
            .pointerInput(Unit) {
                onRightClick {
                    isExpand = true
                    offset = DpOffset(it.x.dp, it.y.dp)
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
        Box(
            modifier = Modifier.padding(top = offset.y).offset(x = offset.x - 110.dp)
        ) {
            DropdownMenu(
                isExpand,
                onDismissRequest = { isExpand = false }
            ) {
                DropdownMenuItem(onClick = {
                    isExpand = false
                    // open maybe slow
                    Desktop.getDesktop().open((MiraiCompose.rootPath / "plugins").toFile())
                }) {
                    Text(R.String.openPluginFolder)
                }
            }
        }
        Row(modifier = Modifier.align(Alignment.BottomEnd)) {
            // TODO: 做成圆形
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
                    pluginList.onAddPluginClick(fc.selectedFile)
                }) {
                Text(R.String.addPlugin)
            }
        }
    }
}
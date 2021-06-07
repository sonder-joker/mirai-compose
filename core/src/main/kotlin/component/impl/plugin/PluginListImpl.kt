package com.youngerhousea.miraicompose.core.component.impl.plugin

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.core.component.plugin.PluginList
import com.youngerhousea.miraicompose.core.console.MiraiCompose
import net.mamoe.mirai.console.plugin.Plugin

internal class PluginListImpl(
    componentContext: ComponentContext,
    override val onPluginCardClick: (plugin: Plugin) -> Unit
) : PluginList, ComponentContext by componentContext {
//    val scope = componentScope()

    override val plugins: List<Plugin> = MiraiCompose.loadedPlugins


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
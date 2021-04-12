package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.*
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.console.MiraiConsoleRepository
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent
import net.mamoe.mirai.console.plugin.Plugin


class LoadedPlugin(
    component: ComponentContext,
    val repository: MiraiConsoleRepository,
) : ComponentContext by component {

    sealed class Configuration : Parcelable {
        object List : Configuration()
        class Detailed(val plugin: Plugin) : Configuration()
    }

    private val router = router<Configuration, Component>(
        initialConfiguration = Configuration.List,
        key = "PluginRouter",
        handleBackButton = true,
        childFactory = { configuration: Configuration, componentContext ->
            when (configuration) {
                is Configuration.List ->
                    PluginList(
                        componentContext,
                        plugins = repository.jvmPluginList,
                        onItemSelected = ::onItemSelected,
                    ).asComponent { PluginListUi(it) }

                is Configuration.Detailed -> {
                    PluginDetailed(
                        componentContext,
                        plugin = configuration.plugin,
                        data = repository.getDataWithConfig(configuration.plugin),
                        onExit = ::onExit
                    ).asComponent { PluginDetailedUi(it) }
                }
            }
        })

    val state get() = router.state

    private fun onItemSelected(plugin: Plugin) {
        router.push(Configuration.Detailed(plugin))
    }

    private fun onExit() {
        router.pop()
    }
}

@OptIn(ExperimentalDecomposeApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
fun LoadedPluginUi(loadedPlugin: LoadedPlugin) {
    Children(loadedPlugin.state, animation = { c, content ->
        AnimatedVisibility(c.activeChild.configuration is PluginDetailed.Setting) {
            content(c.activeChild)
        }
    }) { child ->
        child.instance()
    }
}


package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.*
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfadeScale
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.console.MiraiConsoleRepository
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent
import net.mamoe.mirai.console.plugin.Plugin


class Plugins(
    component: ComponentContext,
    val repository: MiraiConsoleRepository
) : ComponentContext by component {

    private inline val plugins get() = repository.loadedPlugins

    sealed class Configuration : Parcelable {
        object List : Configuration()
        class Specific(val plugin: Plugin) : Configuration()
    }

    private val router: Router<Configuration, Component> = router(
        initialConfiguration = Configuration.List,
        key = "PluginRouter",
        handleBackButton = true,
        childFactory = { configuration: Configuration, componentContext ->
            when (configuration) {
                is Configuration.List ->
                    PluginList(
                        componentContext,
                        plugins = plugins,
                        onPluginCardSelected = ::onPluginSelected,
                    ).asComponent { PluginListUi(it) }

                is Configuration.Specific -> {
                    SpecificPlugin(
                        componentContext,
                        plugin = configuration.plugin,
                        onExitButtonClicked = ::popToPluginList,
                        repository = repository
                    ).asComponent { SpecificPluginUi(it) }
                }
            }
        })

    val state get() = router.state

    private fun onPluginSelected(plugin: Plugin) {
        router.push(Configuration.Specific(plugin))
    }

    private fun popToPluginList() {
        router.pop()
    }
}

@OptIn(ExperimentalDecomposeApi::class, ExperimentalAnimationApi::class)
@Composable
fun PluginsUi(plugins: Plugins) {
    Children(plugins.state, animation = crossfadeScale()) { child ->
        child.instance()
    }
}


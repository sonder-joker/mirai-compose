package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import com.arkivanov.decompose.*
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfadeScale
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.console.MiraiComposeRepository
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent
import net.mamoe.mirai.console.plugin.Plugin


class Plugins(
    component: ComponentContext,
    miraiComposeRepository: MiraiComposeRepository,
) : ComponentContext by component {

    private val plugins = miraiComposeRepository.loadedPlugins

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
                        accessibleHolder = miraiComposeRepository
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
    Box(Modifier.clipToBounds()) {
        Children(plugins.state, animation = crossfadeScale()) { child ->
            child.instance()
        }
    }
}


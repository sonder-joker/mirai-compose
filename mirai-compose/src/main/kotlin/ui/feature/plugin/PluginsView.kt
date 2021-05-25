package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.*
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfadeScale
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent
import net.mamoe.mirai.console.plugin.Plugin

/**
 * 插件菜单
 *
 * @see PluginList
 * @see SpecificPlugin
 */
class Plugins(
    component: ComponentContext,
) : ComponentContext by component {
    private val router: Router<Configuration, Component> = router(
        initialConfiguration = Configuration.List,
        key = "PluginRouter",
        handleBackButton = true,
        childFactory = { configuration: Configuration, componentContext ->
            when (configuration) {
                is Configuration.List ->
                    PluginList(
                        componentContext,
                        onPluginCardClick = ::routToSpecificPlugin
                    ).asComponent { PluginListUi(it) }

                is Configuration.Specific -> {
                    SpecificPlugin(
                        componentContext,
                        plugin = configuration.plugin,
                        onExitButtonClicked = ::popToPluginList
                    ).asComponent { SpecificPluginUi(it) }
                }
            }
        })

    val state get() = router.state

    private fun routToSpecificPlugin(plugin: Plugin) {
        router.push(Configuration.Specific(plugin))
    }

    private fun popToPluginList() {
        router.pop()
    }

    sealed class Configuration : Parcelable {
        object List : Configuration()
        class Specific(val plugin: Plugin) : Configuration()
    }
}

@OptIn(ExperimentalDecomposeApi::class, ExperimentalAnimationApi::class)
@Composable
fun PluginsUi(plugins: Plugins) {
    Children(plugins.state, animation = crossfadeScale()) { child ->
        child.instance()
    }
}


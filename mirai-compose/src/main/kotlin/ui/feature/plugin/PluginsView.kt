package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import com.arkivanov.decompose.*
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfadeScale
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.console.MiraiComposeRepository
import com.youngerhousea.miraicompose.future.inject
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent
import net.mamoe.mirai.console.plugin.Plugin
import org.koin.core.qualifier.named

/**
 * 插件菜单
 *
 * @see PluginList
 * @see SpecificPlugin
 */
class Plugins(
    component: ComponentContext,
) : ComponentContext by component {
    val repository by inject<MiraiComposeRepository>()
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
    Box(Modifier.clipToBounds()) {
        Children(plugins.state, animation = crossfadeScale()) { child ->
            child.instance()
        }
        Button(
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = {
                plugins.repository.reload()
            }) {
            Text("Reload")
        }
    }
}


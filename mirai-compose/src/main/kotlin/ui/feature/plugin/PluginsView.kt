package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.pop
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.console.ReadablePluginDataStorage
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.CrossFade
import com.youngerhousea.miraicompose.utils.asComponent
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.data.PluginDataHolder
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin


class LoadedPlugin(
    component: ComponentContext,
    val plugins: List<Plugin>,
    val configStorage: ReadablePluginDataStorage,
    val dataStorage: ReadablePluginDataStorage,
) : ComponentContext by component {
    private inline val Plugin.data: List<PluginData>
        get() =
            if (this is PluginDataHolder) dataStorage[this] else error("Plugin is not data holder!")

    private inline val Plugin.config: List<PluginData>
        get() =
            if (this is PluginDataHolder) configStorage[this] else error("Plugin is not data holder!")

    private inline val Plugin.dataWithConfig: List<PluginData>
        get() = this.data + this.config

    private val router = router<Configuration, Component>(
        initialConfiguration = Configuration.List,
        key = "PluginRouter",
        handleBackButton = true,
        componentFactory = { configuration: Configuration, ComponentContext ->
            when (configuration) {
                is Configuration.List ->
                    PluginList(
                        ComponentContext,
                        plugins,
                        onItemSelected = ::onItemSelected,
                    ).asComponent { PluginListUi(it) }

                is Configuration.Detailed -> {
                    PluginDetailed(
                        ComponentContext,
                        plugin = configuration.plugin as JvmPlugin,
                        data = configuration.plugin.dataWithConfig,
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


    sealed class Configuration : Parcelable {
        object List : Configuration()
        class Detailed(val plugin: Plugin) : Configuration()
    }

}

@Composable
fun LoadedPluginUi(loadedPlugin: LoadedPlugin) {
    Children(loadedPlugin.state, animation = CrossFade()) { child, _ ->
        child()
    }
}

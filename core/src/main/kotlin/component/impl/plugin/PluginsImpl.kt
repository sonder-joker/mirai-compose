package com.youngerhousea.miraicompose.core.component.impl.plugin

import com.arkivanov.decompose.*
import com.youngerhousea.miraicompose.core.component.plugin.Plugins
import net.mamoe.mirai.console.plugin.Plugin

internal class PluginsImpl(
    component: ComponentContext,
) : Plugins, ComponentContext by component {
    private val router: Router<Plugins.Configuration, ComponentContext> = router(
        initialConfiguration = Plugins.Configuration.List,
        key = "PluginRouter",
        handleBackButton = true,
        childFactory = { configuration: Plugins.Configuration, componentContext ->
            when (configuration) {
                is Plugins.Configuration.List ->
                    PluginListImpl(
                        componentContext,
                        onPluginCardClick = ::routToSpecificPlugin
                    )

                is Plugins.Configuration.Specific -> {
                    SpecificPluginImpl(
                        componentContext,
                        plugin = configuration.plugin,
                        onExitButtonClicked = ::popToPluginList
                    )
                }
            }
        })

    override val state get() = router.state

    private fun routToSpecificPlugin(plugin: Plugin) {
        router.push(Plugins.Configuration.Specific(plugin))
    }

    private fun popToPluginList() {
        router.pop()
    }
}
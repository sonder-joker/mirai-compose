package com.youngerhousea.miraicompose.component.impl.plugin

import com.arkivanov.decompose.*
import com.youngerhousea.miraicompose.component.plugin.Plugins
import com.youngerhousea.miraicompose.ui.plugin.PluginListUi
import com.youngerhousea.miraicompose.ui.plugin.SpecificPluginUi
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent
import net.mamoe.mirai.console.plugin.Plugin

class PluginsImpl(
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
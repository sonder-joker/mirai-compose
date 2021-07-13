package com.youngerhousea.miraicompose.core.component.impl.plugin

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value
import com.youngerhousea.miraicompose.core.component.plugin.Plugins
import com.youngerhousea.miraicompose.core.console.AccessibleHolder
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.PluginManager

internal class PluginsImpl(
    component: ComponentContext,
    accessibleHolder: AccessibleHolder
) : Plugins, ComponentContext by component {

    private sealed class Configuration : Parcelable {
        object List : Configuration()
        class Specific(val plugin: Plugin) : Configuration()
    }

    private val router: Router<Configuration, Plugins.Children> = router(
        initialConfiguration = Configuration.List,
        key = "PluginRouter",
        handleBackButton = true,
        childFactory = { configuration: Configuration, componentContext ->
            when (configuration) {
                is Configuration.List ->
                    Plugins.Children.List(
                        PluginListImpl(
                            componentContext,
                            _onPluginCardClick = ::routeToSpecificPlugin,
                            plugins = PluginManager.plugins
                        )
                    )

                is Configuration.Specific -> {
                    Plugins.Children.Specific(
                        SpecificPluginImpl(
                            componentContext,
                            plugin = configuration.plugin,
                            _onExitButtonClicked = ::popToPluginList,
                            accessibleHolder = accessibleHolder
                        )
                    )
                }
            }
        })

    override val state: Value<RouterState<*, Plugins.Children>> get() = router.state

    private fun routeToSpecificPlugin(plugin: Plugin) {
        router.push(Configuration.Specific(plugin))
    }

    private fun popToPluginList() {
        router.pop()
    }
}
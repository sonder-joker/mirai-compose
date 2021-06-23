package com.youngerhousea.miraicompose.core.component.impl.plugin

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.Router
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value
import com.youngerhousea.miraicompose.core.component.plugin.SpecificPlugin
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

internal class SpecificPluginImpl(
    component: ComponentContext,
    override val plugin: Plugin,
    private inline val _onExitButtonClicked: () -> Unit,
) : SpecificPlugin, ComponentContext by component {
    private sealed class Configuration : Parcelable {
        class Common(val plugin: Plugin) : Configuration()
        class Java(val javaPlugin: JavaPlugin) : Configuration()
        class Kotlin(val kotlinPlugin: KotlinPlugin) : Configuration()
    }

    private val router: Router<Configuration, SpecificPlugin.Children> = router(
        initialConfiguration = when (plugin) {
            is JavaPlugin -> Configuration.Java(plugin)
            is KotlinPlugin -> Configuration.Kotlin(plugin)
            else -> Configuration.Common(plugin)
        },
        handleBackButton = true,
        key = "SpecificPluginRouter",
        childFactory = { configuration, componentContext ->
            when (configuration) {
                is Configuration.Common -> SpecificPlugin.Children.Common(
                    CommonPluginImpl(
                        componentContext,
                        configuration.plugin
                    )
                )
                is Configuration.Java -> SpecificPlugin.Children.Java(
                    CJavaPluginImpl(
                        componentContext,
                        plugin = configuration.javaPlugin,
                    )
                )
                is Configuration.Kotlin -> SpecificPlugin.Children.Kotlin(
                    CKotlinPluginImpl(
                        componentContext,
                        plugin = configuration.kotlinPlugin,
                    )
                )
            }
        }
    )

    override val state: Value<RouterState<*, SpecificPlugin.Children>> get() = router.state

    override fun onExitButtonClicked() {
        _onExitButtonClicked()
    }
}
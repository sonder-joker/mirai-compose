package com.youngerhousea.miraicompose.component.impl.plugin

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.Router
import com.arkivanov.decompose.router
import com.youngerhousea.miraicompose.component.plugin.SpecificPlugin
import com.youngerhousea.miraicompose.ui.plugin.CJvmPluginUi
import com.youngerhousea.miraicompose.ui.plugin.CommonPluginUi
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

class SpecificPluginImpl(
    component: ComponentContext,
    override val plugin: Plugin,
    override val onExitButtonClicked: () -> Unit,
) : SpecificPlugin, ComponentContext by component {
    private val router: Router<SpecificPlugin.Configuration, Component> = router(
        initialConfiguration = when (plugin) {
            is JavaPlugin -> SpecificPlugin.Configuration.Java(plugin)
            is KotlinPlugin -> SpecificPlugin.Configuration.Kotlin(plugin)
            else -> SpecificPlugin.Configuration.Common(plugin)
        },
        handleBackButton = true,
        key = "SpecificPluginRouter",
        childFactory = { configuration, componentContext ->
            when (configuration) {
                is SpecificPlugin.Configuration.Common -> CommonPluginImpl(
                    componentContext,
                    configuration.plugin
                ).asComponent {
                    CommonPluginUi(it)
                }
                is SpecificPlugin.Configuration.Java -> CJvmPluginImpl(
                    componentContext,
                    plugin = configuration.javaPlugin,
                ).asComponent {
                    CJvmPluginUi(it)
                }
                is SpecificPlugin.Configuration.Kotlin -> CJvmPluginImpl(
                    componentContext,
                    plugin = configuration.kotlinPlugin,
                ).asComponent {
                    CJvmPluginUi(it)
                }
            }
        }
    )

    override val state get() = router.state
}
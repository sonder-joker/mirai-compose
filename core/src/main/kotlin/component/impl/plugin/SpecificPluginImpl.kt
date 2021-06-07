package com.youngerhousea.miraicompose.core.component.impl.plugin

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.Router
import com.arkivanov.decompose.router
import com.youngerhousea.miraicompose.core.component.impl.plugin.CJavaPluginImpl
import com.youngerhousea.miraicompose.core.component.impl.plugin.CKotlinPluginImpl
import com.youngerhousea.miraicompose.core.component.impl.plugin.CommonPluginImpl
import com.youngerhousea.miraicompose.core.component.plugin.SpecificPlugin
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

internal class SpecificPluginImpl(
    component: ComponentContext,
    override val plugin: Plugin,
    override val onExitButtonClicked: () -> Unit,
) : SpecificPlugin, ComponentContext by component {
    private val router: Router<SpecificPlugin.Configuration, ComponentContext> = router(
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
                )
                is SpecificPlugin.Configuration.Java -> CJavaPluginImpl(
                    componentContext,
                    plugin = configuration.javaPlugin,
                )
                is SpecificPlugin.Configuration.Kotlin -> CKotlinPluginImpl(
                    componentContext,
                    plugin = configuration.kotlinPlugin,
                )
            }
        }
    )

    override val state get() = router.state
}
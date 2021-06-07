package com.youngerhousea.miraicompose.core.component.impl.plugin

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.Router
import com.arkivanov.decompose.router
import com.youngerhousea.miraicompose.core.component.impl.plugin.shared.DetailedDescriptionImpl
import com.youngerhousea.miraicompose.core.component.plugin.CommonPlugin
import net.mamoe.mirai.console.plugin.Plugin

internal class CommonPluginImpl(
    componentContext: ComponentContext,
    val plugin: Plugin,
) : CommonPlugin, ComponentContext by componentContext {
    private val router: Router<CommonPlugin.Configuration, ComponentContext> = router(
        initialConfiguration = CommonPlugin.Configuration.Description,
        handleBackButton = true,
        key = "PluginDetailedRouter",
        childFactory = { configuration, componentContext ->
            when (configuration) {
                is CommonPlugin.Configuration.Description ->
                    DetailedDescriptionImpl(componentContext, plugin)
            }
        }
    )

    override val state get() = router.state
}
package com.youngerhousea.miraicompose.component.impl.plugin

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.Router
import com.arkivanov.decompose.router
import com.youngerhousea.miraicompose.component.impl.plugin.shared.DetailedDescriptionImpl
import com.youngerhousea.miraicompose.component.plugin.CommonPlugin
import com.youngerhousea.miraicompose.ui.plugin.DetailedDescriptionUi
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent
import net.mamoe.mirai.console.plugin.Plugin

class CommonPluginImpl(
    componentContext: ComponentContext,
    val plugin: Plugin,
) : CommonPlugin, ComponentContext by componentContext {
    private val router: Router<CommonPlugin.Configuration, Component> = router(
        initialConfiguration = CommonPlugin.Configuration.Description,
        handleBackButton = true,
        key = "PluginDetailedRouter",
        childFactory = { configuration, componentContext ->
            when (configuration) {
                is CommonPlugin.Configuration.Description ->
                    DetailedDescriptionImpl(componentContext, plugin).asComponent { DetailedDescriptionUi(it) }
            }
        }
    )

    override val state get() = router.state
}
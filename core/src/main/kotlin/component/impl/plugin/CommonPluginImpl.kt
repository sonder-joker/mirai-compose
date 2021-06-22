package com.youngerhousea.miraicompose.core.component.impl.plugin

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.Router
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value
import com.youngerhousea.miraicompose.core.component.impl.plugin.shared.DetailedDescriptionImpl
import com.youngerhousea.miraicompose.core.component.plugin.CommonPlugin
import net.mamoe.mirai.console.plugin.Plugin

internal class CommonPluginImpl(
    componentContext: ComponentContext,
    val plugin: Plugin,
) : CommonPlugin, ComponentContext by componentContext {
    private sealed class Configuration : Parcelable {
        object Description : Configuration()
    }

    private val router: Router<Configuration, CommonPlugin.Children> = router(
        initialConfiguration = Configuration.Description,
        handleBackButton = true,
        key = "PluginDetailedRouter",
        childFactory = { configuration, componentContext ->
            when (configuration) {
                is Configuration.Description -> {
                    CommonPlugin.Children.Description(
                        DetailedDescriptionImpl(componentContext, plugin)
                    )
                }
            }
        }
    )

    override val state: Value<RouterState<*, CommonPlugin.Children>> get() = router.state
}
package com.youngerhousea.miraicompose.core.component.impl.plugin

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.Router
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.youngerhousea.miraicompose.core.component.impl.plugin.shared.DetailedCommandImpl
import com.youngerhousea.miraicompose.core.component.impl.plugin.shared.DetailedDataImpl
import com.youngerhousea.miraicompose.core.component.impl.plugin.shared.DetailedDescriptionImpl
import com.youngerhousea.miraicompose.core.component.plugin.CJavaPlugin
import com.youngerhousea.miraicompose.core.console.AccessibleHolder
import com.youngerhousea.miraicompose.core.console.MiraiCompose
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.registeredCommands
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin

internal class CJavaPluginImpl(
    componentContext: ComponentContext,
    val plugin: JavaPlugin,
) : CJavaPlugin, ComponentContext by componentContext, AccessibleHolder by MiraiCompose {
    private val router: Router<CJavaPlugin.Configuration, ComponentContext> = router(
        initialConfiguration = CJavaPlugin.Configuration.Description,
        handleBackButton = true,
        key = "PluginDetailedRouter",
        childFactory = { configuration, componentContext ->
            when (configuration) {
                is CJavaPlugin.Configuration.Description ->
                    DetailedDescriptionImpl(componentContext, plugin)
                is CJavaPlugin.Configuration.Data ->
                    DetailedDataImpl(
                        componentContext,
                        data = plugin.data + plugin.config
                    )
                is CJavaPlugin.Configuration.Command ->
                    DetailedCommandImpl(componentContext, plugin.registeredCommands)
            }
        }
    )

    override val state get() = router.state

    override fun onDescriptionClick() {
        router.push(CJavaPlugin.Configuration.Description)
    }

    override fun onDataClick() {
        router.push(CJavaPlugin.Configuration.Data)
    }

    override fun onCommandClick() {
        router.push(CJavaPlugin.Configuration.Command)
    }

}
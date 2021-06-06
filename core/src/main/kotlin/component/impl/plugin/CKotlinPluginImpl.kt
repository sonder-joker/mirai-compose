package com.youngerhousea.miraicompose.core.component.impl.plugin

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.Router
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.youngerhousea.miraicompose.core.component.impl.plugin.shared.DetailedCommandImpl
import com.youngerhousea.miraicompose.core.component.impl.plugin.shared.DetailedDataImpl
import com.youngerhousea.miraicompose.core.component.impl.plugin.shared.DetailedDescriptionImpl
import com.youngerhousea.miraicompose.core.component.plugin.CKotlinPlugin
import com.youngerhousea.miraicompose.core.console.AccessibleHolder
import com.youngerhousea.miraicompose.core.console.MiraiCompose
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.registeredCommands
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

class CKotlinPluginImpl(
    componentContext: ComponentContext,
    val plugin: KotlinPlugin,
) : CKotlinPlugin, ComponentContext by componentContext, AccessibleHolder by MiraiCompose {
    private val router: Router<CKotlinPlugin.Configuration, ComponentContext> = router(
        initialConfiguration = CKotlinPlugin.Configuration.Description,
        handleBackButton = true,
        key = "PluginDetailedRouter",
        childFactory = { configuration, componentContext ->
            when (configuration) {
                is CKotlinPlugin.Configuration.Description ->
                    DetailedDescriptionImpl(componentContext, plugin)
                is CKotlinPlugin.Configuration.Data ->
                    DetailedDataImpl(
                        componentContext,
                        data = plugin.data + plugin.config
                    )
                is CKotlinPlugin.Configuration.Command ->
                    DetailedCommandImpl(componentContext, plugin.registeredCommands)
            }
        }
    )

    override val state get() = router.state

    override fun onDescriptionClick() {
        router.push(CKotlinPlugin.Configuration.Description)
    }

    override fun onDataClick() {
        router.push(CKotlinPlugin.Configuration.Data)
    }

    override fun onCommandClick() {
        router.push(CKotlinPlugin.Configuration.Command)
    }

}
package com.youngerhousea.miraicompose.component.impl.plugin

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.Router
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.youngerhousea.miraicompose.component.impl.plugin.shared.DetailedCommandImpl
import com.youngerhousea.miraicompose.component.impl.plugin.shared.DetailedDataImpl
import com.youngerhousea.miraicompose.component.impl.plugin.shared.DetailedDescriptionImpl
import com.youngerhousea.miraicompose.component.plugin.CJvmPlugin
import com.youngerhousea.miraicompose.console.AccessibleHolder
import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.ui.plugin.DetailedCommandUi
import com.youngerhousea.miraicompose.ui.plugin.DetailedDataUi
import com.youngerhousea.miraicompose.ui.plugin.DetailedDescriptionUi
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent
import net.mamoe.mirai.console.ConsoleFrontEndImplementation
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.registeredCommands
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin

@ConsoleFrontEndImplementation
class CJvmPluginImpl(
    componentContext: ComponentContext,
    val plugin: JvmPlugin,
) : CJvmPlugin, ComponentContext by componentContext, AccessibleHolder by MiraiCompose {
    private val router: Router<CJvmPlugin.Configuration, Component> = router(
        initialConfiguration = CJvmPlugin.Configuration.Description,
        handleBackButton = true,
        key = "PluginDetailedRouter",
        childFactory = { configuration, componentContext ->
            when (configuration) {
                is CJvmPlugin.Configuration.Description ->
                    DetailedDescriptionImpl(componentContext, plugin).asComponent { DetailedDescriptionUi(it) }
                is CJvmPlugin.Configuration.Data ->
                    DetailedDataImpl(
                        componentContext,
                        data = plugin.data + plugin.config
                    ).asComponent { DetailedDataUi(it) }
                is CJvmPlugin.Configuration.Command ->
                    DetailedCommandImpl(componentContext, plugin.registeredCommands).asComponent { DetailedCommandUi(it) }
            }
        }
    )

    override val state get() = router.state

    override fun onDescriptionClick() {
        router.push(CJvmPlugin.Configuration.Description)
    }

    override fun onDataClick() {
        router.push(CJvmPlugin.Configuration.Data)
    }

    override fun onCommandClick() {
        router.push(CJvmPlugin.Configuration.Command)
    }

}
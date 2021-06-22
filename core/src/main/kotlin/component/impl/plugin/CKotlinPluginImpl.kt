package com.youngerhousea.miraicompose.core.component.impl.plugin

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value
import com.youngerhousea.miraicompose.core.component.impl.plugin.shared.DetailedCommandImpl
import com.youngerhousea.miraicompose.core.component.impl.plugin.shared.DetailedDataImpl
import com.youngerhousea.miraicompose.core.component.impl.plugin.shared.DetailedDescriptionImpl
import com.youngerhousea.miraicompose.core.component.plugin.CKotlinPlugin
import com.youngerhousea.miraicompose.core.console.AccessibleHolder
import com.youngerhousea.miraicompose.core.console.MiraiCompose
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.registeredCommands
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

internal class CKotlinPluginImpl(
    componentContext: ComponentContext,
    val plugin: KotlinPlugin,
) : CKotlinPlugin, ComponentContext by componentContext, AccessibleHolder by MiraiCompose.instance {
    private sealed class Configuration : Parcelable {
        object Description : Configuration()
        object Command : Configuration()
        object Data : Configuration()
    }

    private val router: Router<Configuration, CKotlinPlugin.Children> = router(
        initialConfiguration = Configuration.Description,
        handleBackButton = true,
        key = "PluginDetailedRouter",
        childFactory = { configuration, componentContext ->
            when (configuration) {
                is Configuration.Description ->
                    CKotlinPlugin.Children.Description(DetailedDescriptionImpl(componentContext, plugin))
                is Configuration.Data ->
                    CKotlinPlugin.Children.Data(
                        DetailedDataImpl(
                            componentContext,
                            data = plugin.data + plugin.config
                        )
                    )
                is Configuration.Command ->
                    CKotlinPlugin.Children.Command(DetailedCommandImpl(componentContext, plugin.registeredCommands))
            }
        }
    )

    override val state: Value<RouterState<*, CKotlinPlugin.Children>> get() = router.state

    override fun onDescriptionClick() {
        router.push(Configuration.Description)
    }

    override fun onDataClick() {
        router.push(Configuration.Data)
    }

    override fun onCommandClick() {
        router.push(Configuration.Command)
    }

}
package com.youngerhousea.miraicompose.core.component.impl.plugin

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value
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
) : CJavaPlugin, ComponentContext by componentContext, AccessibleHolder by MiraiCompose.instance {
    private sealed class Configuration : Parcelable {
        object Description : Configuration()
        object Command : Configuration()
        object Data : Configuration()
    }

    private val router: Router<Configuration, CJavaPlugin.Children> = router(
        initialConfiguration = Configuration.Description,
        handleBackButton = true,
        key = "PluginDetailedRouter",
        childFactory = { configuration, componentContext ->
            when (configuration) {
                is Configuration.Description ->
                    CJavaPlugin.Children.Description(
                        DetailedDescriptionImpl(componentContext, plugin)
                    )
                is Configuration.Data ->
                    CJavaPlugin.Children.Data(
                        DetailedDataImpl(
                            componentContext,
                            data = plugin.data + plugin.config
                        )
                    )
                is Configuration.Command ->
                    CJavaPlugin.Children.Command(
                        DetailedCommandImpl(componentContext, plugin.registeredCommands)
                    )
            }
        }
    )

    override val state: Value<RouterState<*, CJavaPlugin.Children>> get() = router.state

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
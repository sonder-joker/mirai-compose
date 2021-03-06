package com.youngerhousea.miraicompose.core.component.impl.plugin.shared

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.core.component.plugin.shared.DetailedCommand
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors

@OptIn(ExperimentalCommandDescriptors::class)
internal class DetailedCommandImpl(
    componentContext: ComponentContext,
    override val commands: List<Command>,
) : DetailedCommand, ComponentContext by componentContext {

}

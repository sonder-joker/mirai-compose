package com.youngerhousea.miraicompose.core.component.impl.plugin.shared

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.core.component.plugin.shared.DetailedDescription
import net.mamoe.mirai.console.plugin.Plugin

class DetailedDescriptionImpl(
    componentContext: ComponentContext,
    override val plugin: Plugin
) : DetailedDescription, ComponentContext by componentContext
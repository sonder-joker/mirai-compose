package com.youngerhousea.miraicompose.core.component.impl.about

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.core.component.about.About
import com.youngerhousea.miraicompose.core.console.MiraiComposeDescription
import net.mamoe.mirai.console.MiraiConsole

internal class AboutImpl(
    componentContext: ComponentContext
) : About, ComponentContext by componentContext {

    override val frontend = "Frontend V ${MiraiComposeDescription.version}"

    override val backend = "Backend V ${MiraiConsole.version}"

    val miraiForum = "https://mirai.mamoe.net/"
}
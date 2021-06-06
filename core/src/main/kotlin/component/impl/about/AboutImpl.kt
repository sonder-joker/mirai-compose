package com.youngerhousea.miraicompose.core.component.impl.about

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.core.component.about.About
import com.youngerhousea.miraicompose.core.console.MiraiCompose
import net.mamoe.mirai.console.MiraiConsole

class AboutImpl(
    componentContext: ComponentContext
) : About, ComponentContext by componentContext {

    override val frontend = "Frontend V ${MiraiCompose.frontEndDescription.version}"

    override val backend = "Backend V ${MiraiConsole.version}"

    val miraiForum = "https://mirai.mamoe.net/"
}
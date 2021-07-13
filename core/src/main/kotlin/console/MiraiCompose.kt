package com.youngerhousea.miraicompose.core.console

import net.mamoe.mirai.console.MiraiConsoleFrontEndDescription
import net.mamoe.mirai.console.util.SemVersion


object MiraiComposeDescription : MiraiConsoleFrontEndDescription {
    override val name: String = "MiraiCompose"
    override val vendor: String = "Noire"
    override val version: SemVersion = SemVersion("1.0.0")
}


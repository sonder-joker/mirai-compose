package com.youngerhousea.miraicompose

import com.youngerhousea.miraicompose.console.MiraiCompose
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start
import org.koin.core.component.KoinApiExtension

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.module

object MiraiConsoleComposeLoader {
    @JvmStatic
    fun main(vararg arg: String) {
        MiraiCompose.start()
    }
}




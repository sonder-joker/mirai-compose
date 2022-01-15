package com.youngerhousea.mirai.compose

import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import org.junit.Test

class EntrypointTest{

    @Test
    fun start() {
        startApplication {
            TestPlugin.load()
            TestPlugin.enable()
        }
    }
}
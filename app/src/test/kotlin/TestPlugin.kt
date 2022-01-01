package com.youngerhousea.mirai.compose

import mirai_compose.app.BuildConfig
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

object TestPlugin: KotlinPlugin(
    description = JvmPluginDescription(BuildConfig.projectGroup,BuildConfig.projectVersion, BuildConfig.projectName) {
        author("测试1")
        info("测试2")
    }
) {
    override fun onEnable() {
        logger.info("Link start!")
    }
}
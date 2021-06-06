package com.youngerhousea.miraicompose.core.component.plugin

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin


/**
 * 选择的插件的页面
 *
 * @see CommonPlugin
 * @see CJavaPlugin
 * @see CKotlinPlugin
 */
interface SpecificPlugin {

    val state: Value<RouterState<Configuration, ComponentContext>>

    val plugin: Plugin

    val onExitButtonClicked: () -> Unit

    sealed class Configuration : Parcelable {
        class Common(val plugin: Plugin) : Configuration()
        class Java(val javaPlugin: JavaPlugin) : Configuration()
        class Kotlin(val kotlinPlugin: KotlinPlugin) : Configuration()
    }
}




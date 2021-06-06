package com.youngerhousea.miraicompose.component.plugin

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value
import com.youngerhousea.miraicompose.utils.Component
import net.mamoe.mirai.console.plugin.Plugin


/**
 * 插件菜单
 *
 * @see PluginList
 * @see SpecificPlugin
 */
interface Plugins {

    val state: Value<RouterState<Configuration, ComponentContext>>

    sealed class Configuration : Parcelable {
        object List : Configuration()
        class Specific(val plugin: Plugin) : Configuration()
    }
}

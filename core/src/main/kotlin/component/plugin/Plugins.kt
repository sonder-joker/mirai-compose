package com.youngerhousea.miraicompose.core.component.plugin

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value


/**
 * 插件菜单
 *
 */
interface Plugins {

    val state: Value<RouterState<*, Children>>

    sealed class Children {
        class List(val list: PluginList) : Children()
        class Specific(val specificPlugin: SpecificPlugin) : Children()
    }
}

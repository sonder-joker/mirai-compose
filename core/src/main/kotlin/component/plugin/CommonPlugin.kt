package com.youngerhousea.miraicompose.core.component.plugin

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value


/**
 * 普通插件 一般不应该出现
 *
 */
interface CommonPlugin {
    val state: Value<RouterState<Configuration, ComponentContext>>

    sealed class Configuration : Parcelable {
        object Description : Configuration()
    }
}

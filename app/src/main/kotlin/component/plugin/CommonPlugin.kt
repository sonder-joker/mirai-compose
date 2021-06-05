package com.youngerhousea.miraicompose.component.plugin

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value
import com.youngerhousea.miraicompose.utils.Component


/**
 * 普通插件 一般不应该出现
 *
 */
interface CommonPlugin {
    val state: Value<RouterState<Configuration, Component>>


    sealed class Configuration : Parcelable {
        object Description : Configuration()
    }
}

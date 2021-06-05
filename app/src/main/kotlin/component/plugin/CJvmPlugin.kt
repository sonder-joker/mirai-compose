package com.youngerhousea.miraicompose.component.plugin

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value
import com.youngerhousea.miraicompose.utils.Component


/**
 * Jvm插件的页面
 *
 * @see DetailedDescription
 * @see DetailedData
 * @see DetailedCommand
 */
interface CJvmPlugin {
    val state: Value<RouterState<Configuration, Component>>

    fun onDescriptionClick()

    fun onDataClick()

    fun onCommandClick()

    sealed class Configuration : Parcelable {
        object Description : Configuration()
        object Command : Configuration()
        object Data : Configuration()
    }
}



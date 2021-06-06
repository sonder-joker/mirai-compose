package com.youngerhousea.miraicompose.core.component.plugin

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value


/**
 * Kotlin插件的页面
 *
 * @see DetailedDescription
 * @see DetailedData
 * @see DetailedCommand
 */
interface CKotlinPlugin {
    val state: Value<RouterState<Configuration, ComponentContext>>

    fun onDescriptionClick()

    fun onDataClick()

    fun onCommandClick()

    sealed class Configuration : Parcelable {
        object Description : Configuration()
        object Command : Configuration()
        object Data : Configuration()
    }
}



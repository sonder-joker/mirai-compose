package com.youngerhousea.miraicompose.core.component.plugin

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.youngerhousea.miraicompose.core.component.plugin.shared.DetailedCommand
import com.youngerhousea.miraicompose.core.component.plugin.shared.DetailedData
import com.youngerhousea.miraicompose.core.component.plugin.shared.DetailedDescription


/**
 * Kotlin插件的页面
 *
 */
interface CKotlinPlugin {
    val state: Value<RouterState<*, Children>>

    fun onDescriptionClick()

    fun onDataClick()

    fun onCommandClick()

    sealed class Children {
        class Description(val detailedDescription: DetailedDescription ) : Children()
        class Command(val detailedCommand: DetailedCommand) : Children()
        class Data(val detailedData: DetailedData) : Children()
    }

}



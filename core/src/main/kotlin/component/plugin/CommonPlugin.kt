package com.youngerhousea.miraicompose.core.component.plugin

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.youngerhousea.miraicompose.core.component.plugin.shared.DetailedDescription


/**
 * 普通插件 一般不应该出现
 *
 */
interface CommonPlugin {
    val state: Value<RouterState<*, Children>>

    sealed class Children {
        class Description(val detailedDescription: DetailedDescription) : Children()
    }
}

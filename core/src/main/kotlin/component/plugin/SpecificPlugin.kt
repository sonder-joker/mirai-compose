package com.youngerhousea.miraicompose.core.component.plugin

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import net.mamoe.mirai.console.plugin.Plugin


/**
 * 选择的插件的页面
 *
 */
interface SpecificPlugin {

    val state: Value<RouterState<*, Children>>

    val plugin: Plugin

    fun onExitButtonClicked()

    sealed class Children  {
        class Common(val plugin: CommonPlugin) : Children()
        class Java(val javaPlugin: CJavaPlugin) : Children()
        class Kotlin(val kotlinPlugin: CKotlinPlugin) : Children()
    }
}




package com.youngerhousea.miraicompose.core.component.impl.setting

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.youngerhousea.miraicompose.core.component.setting.MainSetting
import com.youngerhousea.miraicompose.core.component.setting.PluginControlSetting

internal class MainSettingImpl(
    componentContext: ComponentContext,
    private inline val _routeAutoLogin: () -> Unit,
    private inline val _routeLoginColor: () -> Unit,
    private inline val _routeLoginLevel: () -> Unit
) : MainSetting, ComponentContext by componentContext {
    override fun routeAutoLogin() {
        _routeAutoLogin()
    }

    override fun routeLoginColor() {
        _routeLoginColor()
    }

    override fun routeLoginLevel() {
        _routeLoginLevel()
    }

    override val pluginControlSetting: PluginControlSetting = PluginControlSettingImpl(childContext("pluginControl"))

}
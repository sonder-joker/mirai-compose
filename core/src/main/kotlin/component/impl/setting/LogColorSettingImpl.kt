package com.youngerhousea.miraicompose.core.component.impl.setting

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.instancekeeper.getOrCreate
import com.youngerhousea.miraicompose.core.component.setting.LogColorSetting
import com.youngerhousea.miraicompose.core.viewmodel.ThemeViewModel
import kotlin.reflect.KFunction0

internal class LogColorSettingImpl(
    componentContext: ComponentContext,
    private inline val _onExitButtonClicked: () -> Unit,
    private val themeViewModel: ThemeViewModel = componentContext.instanceKeeper.getOrCreate { ThemeViewModel() },
) : LogColorSetting, ComponentContext by componentContext {

    override val logColor = themeViewModel.data

    override fun setDebugColor(debug: String) {
        themeViewModel.setDebugColor(debug)
    }

    override fun setVerboseColor(verbose: String) {
        themeViewModel.setVerboseColor(verbose)
    }

    override fun setInfoColor(info: String) {
        themeViewModel.setInfoColor(info)
    }

    override fun setWarningColor(warning: String) {
        themeViewModel.setWarningColor(warning)
    }

    override fun setErrorColor(error: String) {
        themeViewModel.setErrorColor(error)
    }

    override fun onExitButtonClicked() {
        _onExitButtonClicked()
    }
}
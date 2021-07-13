package com.youngerhousea.miraicompose.core.component.impl.setting

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.instancekeeper.getOrCreate
import com.youngerhousea.miraicompose.core.component.setting.*
import com.youngerhousea.miraicompose.core.console.LogPriority
import com.youngerhousea.miraicompose.core.data.LoginCredential
import com.youngerhousea.miraicompose.core.viewmodel.AutoLoginViewModel
import com.youngerhousea.miraicompose.core.viewmodel.LogPriorityViewModel
import com.youngerhousea.miraicompose.core.viewmodel.Node
import com.youngerhousea.miraicompose.core.viewmodel.ThemeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class SettingImpl(
    componentContext: ComponentContext,
) : Setting, ComponentContext by componentContext {

    override val logLevelSetting = LogLevelSettingImpl(childContext("logLevel"))

    override val logColorSetting = LogColorSettingImpl(childContext("logColor"))

    override val autoLoginSetting = AutoLoginImpl(childContext("autoLogin"))
}

class LogColorSettingImpl(
    componentContext: ComponentContext,
    private val themeViewModel: ThemeViewModel = componentContext.instanceKeeper.getOrCreate { ThemeViewModel() }
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
}

class LogLevelSettingImpl(
    componentContext: ComponentContext,
    private val logPriorityViewModel: LogPriorityViewModel = componentContext.instanceKeeper.getOrCreate { LogPriorityViewModel() }
) : LogLevelSetting, ComponentContext by componentContext {

    override val node: StateFlow<Node> get() = logPriorityViewModel.data

    override fun setLogConfigLevel(priority: LogPriority) {

    }
}


class AutoLoginImpl(
    componentContext: ComponentContext,
    private val autoLoginViewModel: AutoLoginViewModel = componentContext.instanceKeeper.getOrCreate { AutoLoginViewModel() }
) : AutoLoginSetting, ComponentContext by componentContext {

    override val model: StateFlow<List<LoginCredential>> get() = autoLoginViewModel.data

    override fun addAutoLogin(config: LoginCredential) {
        autoLoginViewModel.addAutoLoginAccount(config)
    }

    override fun updateLoginCredential(index: Int, loginCredential: LoginCredential) {
        autoLoginViewModel.updateLoginCredential(index, loginCredential)
    }

    override fun addLoginCredential(loginCredential: LoginCredential) {
        autoLoginViewModel.addAutoLoginAccount(loginCredential)
    }

}


class ThemeColorImpl(
) {

//    fun setPrimary(color: Color) {
//        theme.materialLight.primary = color
//    }
//
//    fun setPrimaryVariant(color: Color) {
//        theme.materialLight.primaryVariant = color
//    }
//
//    fun setSecondary(color: Color) {
//        theme.materialLight.secondary = color
//    }
//
//    fun setSecondaryVariant(color: Color) {
//        theme.materialLight.secondaryVariant = color
//    }
//
//    fun setBackground(color: Color) {
//        material = material.copy(background = color)
//    }
//
//    fun setSurface(color: Color) {
//        material = material.copy(surface = color)
//    }
//
//    fun setError(color: Color) {
//        material = material.copy(error = color)
//    }
//
//    fun setOnPrimary(color: Color) {
//        material = material.copy(onPrimary = color)
//    }
//
//    fun setOnSecondary(color: Color) {
//        material = material.copy(onSecondary = color)
//    }
//
//    fun setOnBackground(color: Color) {
//        material = material.copy(onBackground = color)
//    }
//
//    fun setOnSurface(color: Color) {
//        material = material.copy(onSurface = color)
//    }
//
//    fun setOnError(color: Color) {
//        material = material.copy(onError = color)
//    }
//
//    fun setIsLight(isLight: Boolean) {
//        material = material.copy(isLight = isLight)
//    }
}
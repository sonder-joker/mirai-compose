package com.youngerhousea.miraicompose.core.component.impl.setting

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value
import com.youngerhousea.miraicompose.core.component.setting.Setting

internal class SettingImpl(
    componentContext: ComponentContext,
) : Setting, ComponentContext by componentContext {

    private sealed class Configuration : Parcelable {
        object Main : Configuration()
        object LogLevel : Configuration()
        object LogColor : Configuration()
        object AutoLogin : Configuration()
    }

    override val state: Value<RouterState<*, Setting.Child>> get() = router.state

    private val router = router<Configuration, Setting.Child>(
        initialConfiguration = Configuration.Main,
        handleBackButton = true,
        childFactory = { configuration, componentContext ->
            when (configuration) {
                Configuration.AutoLogin -> Setting.Child.AutoLogin(AutoLoginImpl(componentContext))
                Configuration.LogColor -> Setting.Child.LogColor(LogColorSettingImpl(componentContext))
                Configuration.LogLevel -> Setting.Child.LogLevel(LogLevelSettingImpl(componentContext))
                Configuration.Main -> Setting.Child.Main(
                    MainSettingImpl(
                        componentContext,
                        _routeAutoLogin = ::routeAutoLogin,
                        _routeLoginColor = ::routeLoginColor,
                        _routeLoginLevel = ::routeLoginLevel
                    )
                )
            }
        }
    )

    private fun routeAutoLogin() {
        router.push(Configuration.AutoLogin)
    }

    private fun routeLoginColor() {
        router.push(Configuration.LogColor)
    }

    private fun routeLoginLevel() {
        router.push(Configuration.LogLevel)
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
package com.youngerhousea.miraicompose.component.impl.setting

import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.component.setting.Setting
import com.youngerhousea.miraicompose.theme.AppTheme

class SettingImpl(
    componentContext: ComponentContext,
    val theme: AppTheme
) : Setting, ComponentContext by componentContext {
    private inline val logColors get() = theme.logColors

    private var material = theme.materialLight
        set(value) {
            theme.materialLight = value
            field = value
        }

    override val debug get() = logColors.debug

    override val verbose get() = logColors.verbose

    override val info get() = logColors.info

    override val warning get() = logColors.warning

    override val error get() = logColors.error

    override fun onDebugColorSet(color: Color) {
        logColors.debug = color
    }

    override fun onVerboseColorSet(color: Color) {
        logColors.verbose = color
    }

    override fun onInfoColorSet(color: Color) {
        logColors.info = color
    }

    override fun onWarningColorSet(color: Color) {
        logColors.warning = color
    }

    override fun onErrorColorSet(color: Color) {
        logColors.error = color
    }

    fun setPrimary(color: Color) {
        material = material.copy(primary = color)
    }

    fun setPrimaryVariant(color: Color) {
        material = material.copy(primaryVariant = color)
    }

    fun setSecondary(color: Color) {
        material = material.copy(secondary = color)
    }

    fun setSecondaryVariant(color: Color) {
        material = material.copy(secondaryVariant = color)
    }

    fun setBackground(color: Color) {
        material = material.copy(background = color)
    }

    fun setSurface(color: Color) {
        material = material.copy(surface = color)
    }

    fun setError(color: Color) {
        material = material.copy(error = color)
    }

    fun setOnPrimary(color: Color) {
        material = material.copy(onPrimary = color)
    }

    fun setOnSecondary(color: Color) {
        material = material.copy(onSecondary = color)
    }

    fun setOnBackground(color: Color) {
        material = material.copy(onBackground = color)
    }

    fun setOnSurface(color: Color) {
        material = material.copy(onSurface = color)
    }

    fun setOnError(color: Color) {
        material = material.copy(onError = color)
    }

    fun setIsLight(isLight: Boolean) {
        material = material.copy(isLight = isLight)
    }
}
package com.youngerhousea.miraicompose.core.component.impl.setting

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.youngerhousea.miraicompose.core.component.setting.LogColorSetting
import com.youngerhousea.miraicompose.core.component.setting.LogLevelSetting
import com.youngerhousea.miraicompose.core.component.setting.Setting
import com.youngerhousea.miraicompose.core.component.setting.StringColor
import com.youngerhousea.miraicompose.core.console.LoggerConfig
import com.youngerhousea.miraicompose.core.console.MiraiCompose
import com.youngerhousea.miraicompose.core.theme.AppTheme
import net.mamoe.mirai.console.data.AutoSavePluginDataHolder
import net.mamoe.mirai.console.logging.AbstractLoggerController

@Suppress("INVISIBLE_MEMBER")
internal class SettingImpl(
    componentContext: ComponentContext,
    theme: AppTheme
) : Setting, ComponentContext by componentContext {

    private val consoleBuiltInPluginDataHolder =
        Class.forName("net.mamoe.mirai.console.internal.data.builtins.ConsoleBuiltInPluginDataHolder").kotlin.objectInstance as AutoSavePluginDataHolder
    private val consoleBuiltInPluginConfigHolder =
        Class.forName("net.mamoe.mirai.console.internal.data.builtins.ConsoleBuiltInPluginConfigHolder").kotlin.objectInstance as AutoSavePluginDataHolder


//    override val config = MiraiCompose.instance.configStorageForBuiltIns[consoleBuiltInPluginConfigHolder]
//    override val data = MiraiCompose.instance.dataStorageForBuiltIns[consoleBuiltInPluginDataHolder]

    override val logLevelSetting = LogLevelSettingImpl(childContext("logLevel"))

    override val logColorSetting = LogColorSettingImpl(childContext("logColor"), theme)
}

class LogColorSettingImpl(
    componentContext: ComponentContext,
    private val theme: AppTheme
) : LogColorSetting, ComponentContext by componentContext {

    override val debug = theme.logColors.debug

    override val verbose = theme.logColors.verbose

    override val info get() = theme.logColors.info

    override val warning get() = theme.logColors.warning

    override val error get() = theme.logColors.error

    override fun onDebugColorSet(stringColor: StringColor) {
        theme.logColors.debug = stringColor
    }

    override fun onVerboseColorSet(stringColor: StringColor) {
        theme.logColors.verbose = stringColor
    }

    override fun onInfoColorSet(stringColor: StringColor) {
        theme.logColors.info = stringColor
    }

    override fun onWarningColorSet(stringColor: StringColor) {
        theme.logColors.warning = stringColor
    }

    override fun onErrorColorSet(stringColor: StringColor) {
        theme.logColors.error = stringColor
    }
}

class LogLevelSettingImpl(
    componentContext: ComponentContext
) : LogLevelSetting, ComponentContext by componentContext {
    override val logConfigLevel: AbstractLoggerController.LogPriority get()  = LoggerConfig.defaultPriority


    override fun setLogConfigLevel(priority: AbstractLoggerController.LogPriority) {
        LoggerConfig.defaultPriority = priority
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
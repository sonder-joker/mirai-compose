package com.youngerhousea.miraicompose.core.component.impl.setting

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.youngerhousea.miraicompose.core.component.setting.*
import com.youngerhousea.miraicompose.core.console.LoggerConfig
import com.youngerhousea.miraicompose.core.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.internal.data.builtins.AutoLoginConfig
import net.mamoe.mirai.console.logging.AbstractLoggerController
import net.mamoe.yamlkt.YamlDynamicSerializer

@Suppress("INVISIBLE_MEMBER")
internal class SettingImpl(
    componentContext: ComponentContext,
    theme: AppTheme
) : Setting, ComponentContext by componentContext {

    override val logLevelSetting = LogLevelSettingImpl(childContext("logLevel"), LoggerConfig.defaultPriority)

    override val logColorSetting = LogColorSettingImpl(childContext("logColor"), theme)

    override val autoLoginSetting = AutoLoginImpl(childContext("autoLogin"), AutoLoginConfig.accounts)
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
    componentContext: ComponentContext,
    override val logConfigLevel: AbstractLoggerController.LogPriority,
) : LogLevelSetting, ComponentContext by componentContext {

    override fun setLogConfigLevel(priority: AbstractLoggerController.LogPriority) {
        LoggerConfig.defaultPriority = priority
    }
}

class AutoLoginImpl(
    componentContext: ComponentContext,
    list: List<AutoLoginConfig.Account>,
) : AutoLoginSetting, ComponentContext by componentContext {
    override val model = MutableStateFlow(AutoLoginSetting.Model(list))

    override fun addAutoLogin(
        account: String,
        password: AutoLoginConfig.Account.Password,
        configuration: Map<AutoLoginConfig.Account.ConfigurationKey, @Serializable(with = YamlDynamicSerializer::class) Any>
    ) {
        AutoLoginConfig.accounts.add(AutoLoginConfig.Account(account, password, configuration))
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
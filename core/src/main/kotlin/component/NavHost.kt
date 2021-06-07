package com.youngerhousea.miraicompose.core.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value
import com.youngerhousea.miraicompose.core.component.plugin.Plugins
import com.youngerhousea.miraicompose.core.console.ComposeBot

/**
 * 主界面
 *
 * @property botList 目前的Console所登录的机器人List
 * @property currentBot 目前的显示的机器人
 *
 * @see [Login]
 * @see [Message]
 * @see [Setting]
 * @see [About]
 * @see [ConsoleLog]
 * @see [Plugins]
 */
interface NavHost {
    val botList: List<ComposeBot>

    val currentBot: ComposeBot?

    val state: Value<RouterState<Configuration, ComponentContext>>

    // 登录机器人
    fun addNewBot()

    fun onRouteToSpecificBot(bot: ComposeBot)

    fun onRouteMessage()

    fun onRoutePlugin()

    fun onRouteSetting()

    fun onRouteLog()

    fun onRouteAbout()

    sealed class Configuration : Parcelable {
        object Message : Configuration()
        object Login : Configuration()
        object Setting : Configuration()
        object About : Configuration()
        object ConsoleLog : Configuration()
        object Plugin : Configuration()
    }
}


package com.youngerhousea.miraicompose.core.component

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.youngerhousea.miraicompose.core.component.about.About
import com.youngerhousea.miraicompose.core.component.bot.Login
import com.youngerhousea.miraicompose.core.component.log.ConsoleLog
import com.youngerhousea.miraicompose.core.component.message.Message
import com.youngerhousea.miraicompose.core.component.plugin.Plugins
import com.youngerhousea.miraicompose.core.component.setting.Setting
import kotlinx.coroutines.flow.StateFlow
import net.mamoe.mirai.Bot

/**
 * 主界面
 *
 *
 */
interface NavHost {

    val state: Value<RouterState<*, Child>>

    data class Model(
        val currentBot: BotItem?,
        val isExpand: Boolean,
        val botList: List<BotItem>
    )

    val model: StateFlow<Model>

    fun onRouteMessage()

    fun onRoutePlugin()

    fun onRouteSetting()

    fun onRouteLog()

    fun onRouteAbout()

    fun addNewBot()

    fun openExpandMenu()

    fun dismissExpandMenu()

    fun onAvatarBoxClick()

    fun onItemClick(item: BotItem)

    sealed class Child {
        class CMessage(val message: Message) : Child()
        class CLogin(val login: Login) : Child()
        class CSetting(val setting: Setting) : Child()
        class CAbout(val about: About) : Child()
        class CConsoleLog(val log: ConsoleLog) : Child()
        class CPlugins(val plugins: Plugins) : Child()
    }
}

interface BotItem : Bot {

    val avatar: StateFlow<ByteArray?>
}


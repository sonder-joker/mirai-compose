package com.youngerhousea.miraicompose.core.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.statekeeper.Parcelable
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
 * @property botList 目前的Console所登录的机器人List
 * @property currentBot 目前的显示的机器人
 *
 */
interface NavHost {

    val state: Value<RouterState<*, Child>>

    val avatarMenu: AvatarMenu

    val botList: List<BotItem>

    val currentBot: StateFlow<BotItem?>

    fun onRouteMessage()

    fun onRoutePlugin()

    fun onRouteSetting()

    fun onRouteLog()

    fun onRouteAbout()

    fun onRouteToSpecificBot(bot: BotItem)


    sealed class Child {
        class CMessage(val message:Message):Child()
        class CLogin(val login:Login):Child()
        class CSetting(val setting:Setting):Child()
        class CAbout(val about: About):Child()
        class CConsoleLog(val log:ConsoleLog):Child()
        class CPlugins(val plugins:Plugins):Child()
    }

}

interface AvatarMenu {
    val currentBot: StateFlow<BotItem?>

    val isExpand: StateFlow<Boolean>

    val botList: List<BotItem>

    // 登录机器人
    fun addNewBot()

    fun openExpandMenu()

    fun dismissExpandMenu()

    fun onAvatarBoxClick()

    fun onItemClick(item: BotItem)
}

interface BotItem {
    val bot: Bot

    val avatar: ByteArray?
}
package com.youngerhousea.miraicompose.core.component

import net.mamoe.mirai.Bot

interface BotMenu {
    val currentBot: Bot?

    val composeBotList: List<Bot>

    fun onBoxClick()

    fun onNewBotButtonSelected()

    fun onMenuBotSelected(item: Bot)
}

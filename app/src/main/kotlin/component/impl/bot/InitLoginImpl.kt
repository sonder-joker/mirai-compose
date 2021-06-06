package com.youngerhousea.miraicompose.component.impl.bot

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.component.bot.InitLogin
import com.youngerhousea.miraicompose.utils.componentScope
import kotlinx.coroutines.*
import net.mamoe.mirai.network.*


class InitLoginImpl(
    componentContext: ComponentContext,
    private val onClick: suspend (account: Long, password: String) -> Unit,
) : InitLogin, ComponentContext by componentContext {
    private val scope = componentScope()

    override fun onLogin(account: Long, password: String) {
        scope.launch {
            onClick(account, password)
        }
    }
}

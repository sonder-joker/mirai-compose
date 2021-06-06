package com.youngerhousea.miraicompose.core.component.impl.bot

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.core.component.bot.InitLogin
import com.youngerhousea.miraicompose.core.utils.componentScope
import kotlinx.coroutines.launch


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

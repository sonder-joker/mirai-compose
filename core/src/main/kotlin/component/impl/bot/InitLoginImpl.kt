package com.youngerhousea.miraicompose.core.component.impl.bot

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.core.component.bot.InitLogin
import com.youngerhousea.miraicompose.core.utils.componentScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


internal class InitLoginImpl(
    componentContext: ComponentContext,
    private val onClick: suspend (account: Long, password: String) -> Unit,
) : InitLogin, ComponentContext by componentContext {
    private val scope = componentScope()

    override val data  = MutableStateFlow(InitLogin.Model("", ""))

    override fun onAccountChange(account: String) {
        scope.launch {
            with(data) {
                emit(value.copy(account = account))
            }
        }
    }

    override fun onPasswordChange(password: String) {
        scope.launch {
            data.emit(data.value.copy(password = password))
        }
    }

    override fun onLogin(account: Long, password: String) {
        scope.launch {
            onClick(account, password)
        }
    }

}

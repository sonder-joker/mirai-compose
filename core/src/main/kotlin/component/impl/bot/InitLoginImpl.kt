package com.youngerhousea.miraicompose.core.component.impl.bot

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.core.component.bot.InitLogin
import com.youngerhousea.miraicompose.core.utils.componentScope
import com.youngerhousea.miraicompose.core.utils.getValue
import com.youngerhousea.miraicompose.core.utils.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


internal class InitLoginImpl(
    componentContext: ComponentContext,
    private val onClick: suspend (account: Long, password: String) -> Unit,
) : InitLogin, ComponentContext by componentContext, CoroutineScope by componentContext.componentScope() {

    override val data = MutableStateFlow(InitLogin.Model("", ""))

    var delegateModel by data

    override fun onAccountChange(account: String) {
        delegateModel = delegateModel.copy(account = account)

    }

    override fun onPasswordChange(password: String) {
        delegateModel = delegateModel.copy(password = password)
    }

    override fun onLogin(account: Long, password: String) {
        launch {
            onClick(account, password)
        }
    }

}

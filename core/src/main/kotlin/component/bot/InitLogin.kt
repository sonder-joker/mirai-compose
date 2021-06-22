package com.youngerhousea.miraicompose.core.component.bot

import kotlinx.coroutines.flow.StateFlow


/**
 * bot的登录的界面
 */
interface InitLogin {
    data class Model(
        val account: String,
        val password: String
    )

    val data:StateFlow<Model>

    fun onAccountChange(account: String)

    fun onPasswordChange(password: String)

    fun onLogin(account: Long, password: String)
}
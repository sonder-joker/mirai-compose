package com.youngerhousea.miraicompose.core.component.bot

import kotlinx.coroutines.flow.StateFlow


/**
 * bot的登录的界面
 */
interface InitLogin {
    data class Model(
        val account: String = "",
        val password: String = "",
        val event:Event = Event.Normal
    )

    val model:StateFlow<Model>

    fun onAccountChange(account: String)

    fun onPasswordChange(password: String)

    fun onLogin(account: Long, password: String)

    fun cancelLogin()

}

sealed class Event {
    object Normal:Event()

    class Loading(val message: String):Event()

    class Error(val message:String):Event()
}
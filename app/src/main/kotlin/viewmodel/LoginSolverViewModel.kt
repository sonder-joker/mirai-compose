package com.youngerhousea.mirai.compose.viewmodel

import com.youngerhousea.mirai.compose.console.Login
import com.youngerhousea.mirai.compose.console.ViewModelScope
import com.youngerhousea.mirai.compose.console.impl.MiraiCompose

class LoginSolverViewModel : ViewModelScope() {
    val state = MiraiCompose.loginSolverState

    fun dispatch(login: Login) {
        MiraiCompose.dispatch(login)
    }
}
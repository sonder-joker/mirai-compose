package com.youngerhousea.miraicompose.core.component.setting

import com.youngerhousea.miraicompose.core.data.LoginCredential
import kotlinx.coroutines.flow.StateFlow

interface AutoLoginSetting {

    val loginCredentials: StateFlow<List<LoginCredential>>

    fun addAutoLogin(config: LoginCredential)

    fun updateLoginCredential(index: Int, loginCredential: LoginCredential)

    fun addLoginCredential(loginCredential: LoginCredential)
}
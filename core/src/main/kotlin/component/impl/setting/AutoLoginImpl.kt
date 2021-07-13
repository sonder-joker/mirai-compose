package com.youngerhousea.miraicompose.core.component.impl.setting

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.instancekeeper.getOrCreate
import com.youngerhousea.miraicompose.core.component.setting.AutoLoginSetting
import com.youngerhousea.miraicompose.core.data.LoginCredential
import com.youngerhousea.miraicompose.core.viewmodel.AutoLoginViewModel
import kotlinx.coroutines.flow.StateFlow

internal class AutoLoginImpl(
    componentContext: ComponentContext,
    private val autoLoginViewModel: AutoLoginViewModel = componentContext.instanceKeeper.getOrCreate { AutoLoginViewModel() }
) : AutoLoginSetting, ComponentContext by componentContext {

    override val loginCredentials: StateFlow<List<LoginCredential>> get() = autoLoginViewModel.data

    override fun addAutoLogin(config: LoginCredential) {
        autoLoginViewModel.addAutoLoginAccount(config)
    }

    override fun updateLoginCredential(index: Int, loginCredential: LoginCredential) {
        autoLoginViewModel.updateLoginCredential(index, loginCredential)
    }

    override fun addLoginCredential(loginCredential: LoginCredential) {
        autoLoginViewModel.addAutoLoginAccount(loginCredential)
    }

}
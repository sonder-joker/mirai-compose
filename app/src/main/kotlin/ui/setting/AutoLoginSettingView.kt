package com.youngerhousea.miraicompose.app.ui.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import com.youngerhousea.miraicompose.app.utils.EnumTabRowWithContent
import com.youngerhousea.miraicompose.core.component.setting.AutoLoginSetting
import com.youngerhousea.miraicompose.core.data.LoginCredential


@Composable
private inline fun AutoLoginPage(
    loginCredential: LoginCredential,
    crossinline onSubmit: (loginCredential: LoginCredential) -> Unit
) {
    with(loginCredential) {
        Row {
            TextField(account, onValueChange = {
                onSubmit(loginCredential.copy(account = it))
            })

            TextField(password, onValueChange = {
                onSubmit(loginCredential.copy(password = it))
            })

            EnumTabRowWithContent(passwordKind, onClick = {
                onSubmit(loginCredential.copy(passwordKind = it))
            }) {
                Text(it.name)
            }

            EnumTabRowWithContent(protocolKind, onClick = {
                onSubmit(loginCredential.copy(protocolKind = it))
            }) {
                Text(it.name)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AutoLoginSettingUi(autoLoginSetting: AutoLoginSetting) {
    val accounts by autoLoginSetting.loginCredentials.collectAsState()

    Column {
        Button({
            autoLoginSetting.addAutoLogin(LoginCredential())
        }) {
            Text("Create a auto login account")
        }

        Text("Now Accounts")

        AnimatedVisibility(accounts.isEmpty()) {
            Text("Not have Auto Login Account")
        }

        AnimatedVisibility(accounts.isNotEmpty()) {
            accounts.forEachIndexed { index, loginCredential ->
                AutoLoginPage(loginCredential) {
                    autoLoginSetting.updateLoginCredential(index, loginCredential)
                }
            }
        }
    }
}

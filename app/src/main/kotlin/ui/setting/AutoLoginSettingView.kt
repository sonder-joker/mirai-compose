package com.youngerhousea.miraicompose.app.ui.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.app.utils.EnumTabRowWithContent
import com.youngerhousea.miraicompose.core.component.setting.AutoLoginSetting
import com.youngerhousea.miraicompose.core.data.LoginCredential


@Composable
fun AutoLoginSettingUi(autoLoginSetting: AutoLoginSetting) {
    val accounts by autoLoginSetting.loginCredentials.collectAsState()
    AutoLoginSetting(
        accounts,
        autoLoginSetting::onExitButtonClicked,
        autoLoginSetting::updateLoginCredential,
        autoLoginSetting::addLoginCredential
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AutoLoginSetting(
    accounts: List<LoginCredential>,
    onExit: () -> Unit,
    onEditLoginCredential: (index: Int, loginCredential: LoginCredential) -> Unit,
    onAddAutoLoginCredential: (LoginCredential) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Icon(
                Icons.Default.KeyboardArrowLeft,
                null,
                Modifier.clickable(onClick = onExit)
            )
        }) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Now Accounts")
            AnimatedVisibility(accounts.isEmpty()) {
                Text("Not have Auto Login Accounts")
            }
            AnimatedVisibility(accounts.isNotEmpty()) {
                accounts.forEachIndexed { index, loginCredential ->
                    AutoLoginPage(loginCredential) {
                        onEditLoginCredential(index, loginCredential)
                    }
                }
            }

            Button({
                onAddAutoLoginCredential(LoginCredential())
            }, modifier = Modifier.align(Alignment.End)) {
                Text("Create a auto login account")
            }
        }
    }
}

@Composable
private inline fun AutoLoginPage(
    loginCredential: LoginCredential,
    modifier: Modifier = Modifier,
    crossinline onSubmit: (loginCredential: LoginCredential) -> Unit
) {
    with(loginCredential) {
        Column(modifier.fillMaxSize().padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                TextField(account, onValueChange = {
                    onSubmit(loginCredential.copy(account = it))
                })

                TextField(password, onValueChange = {
                    onSubmit(loginCredential.copy(password = it))
                })
            }

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

@Composable
@Preview
fun AutoLoginSettingPreview() {
    val loginCredentials = remember { mutableStateListOf<LoginCredential>() }
    AutoLoginSetting(
        loginCredentials,
        onExit = {},
        onEditLoginCredential = { index, loginCredential ->
            loginCredentials[index] = loginCredential
        },
        onAddAutoLoginCredential = loginCredentials::add
    )
}
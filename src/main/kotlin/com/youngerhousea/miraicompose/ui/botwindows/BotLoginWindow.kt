
package com.youngerhousea.miraicompose.ui.botwindows

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.model.LoginWindowState
import com.youngerhousea.miraicompose.theme.AppTheme
import com.youngerhousea.miraicompose.theme.ResourceImage
import net.mamoe.mirai.network.WrongPasswordException

@Composable
fun BotLoginWindow(loginWindowState: LoginWindowState, onClick: () -> Unit) =
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            ResourceImage.mirai,
            contentDescription = "Mirai",
            modifier = Modifier
                .padding(5.dp)
        )

        TextField(
            loginWindowState.account,
            onValueChange = {
                if (it.matches("^[0-9]{0,15}$".toRegex())) {
                    loginWindowState.account = it
                    loginWindowState.invalidInputAccount = false
                } else {
                    loginWindowState.invalidInputAccount = true
                }
            },
            modifier = Modifier
                .padding(40.dp),
            isErrorValue = loginWindowState.invalidInputAccount,
            label = {
                if (loginWindowState.invalidInputAccount)
                    Text("账号只能由数字组成")
                else
                    Text("账号")
            })

        TextField(
            loginWindowState.password,
            onValueChange = {
                loginWindowState.password = it
            },
            modifier = Modifier
                .padding(40.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            singleLine = true,
            label = {
                Text("密码")
            },
//            trailingIcon = {}
        )

        BotLoginButton(
            {
                kotlin.runCatching {
                    onClick()
                }.onFailure {
                    loginWindowState.exceptionPrompt = when (it) {
                        is WrongPasswordException -> {
                            "密码错误！"
                        }
                        is NumberFormatException -> {
                            "格式错误！"
                        }
                        else -> {
                            it.printStackTrace()
                            "呜呜呜"
                        }
                    }
                    loginWindowState.isException = true
                }
            }, modifier = Modifier
                .preferredHeight(100.dp)
        ) {
            Text("登录")
        }

        if (loginWindowState.isException) {
            Snackbar { Text(loginWindowState.exceptionPrompt) }
            loginWindowState.isException = false
        }
    }

@Composable
fun BotLoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(2f)
            .padding(24.dp),
        backgroundColor = AppTheme.colors.backgroundDark,
        content = content
    )
}
package com.youngerhousea.miraicompose.ui.feature.bot.botstate

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.shortcuts
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.theme.ResourceImage
import com.youngerhousea.miraicompose.utils.Component
import net.mamoe.mirai.network.RetryLaterException
import net.mamoe.mirai.network.WrongPasswordException

class BotLogin(
    componentContext: ComponentContext,
    private val onClick: (account: Long, password: String) -> Unit
) : Component, ComponentContext by componentContext {
    @Composable
    override fun render() {
        BotLoginView(onClick)
    }
}


class LoginWindowState {
    var invalidInputAccount by mutableStateOf(false)

    var invalidPassword by mutableStateOf("")

    var isException by mutableStateOf(false)

    var account by mutableStateOf(TextFieldValue())

    var password by mutableStateOf(TextFieldValue())
}


@Composable
fun BotLoginView(onClick: (account: Long, password: String) -> Unit) {
    val loginWindowState = remember { LoginWindowState() }

    fun login() {
        runCatching { onClick(loginWindowState.account.text.toLong(), loginWindowState.password.text) }
            .onFailure {
                loginWindowState.invalidPassword = when (it) {
                    is WrongPasswordException -> {
                        "密码错误！"
                    }
                    is NumberFormatException -> {
                        "格式错误！"
                    }
                    is RetryLaterException -> {
                        "请稍后再试"
                    }
                    else -> {
                        it.printStackTrace()
                        "呜呜呜"
                    }
                }
                loginWindowState.isException = true
            }
    }

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
                if (it.text.matches("^[0-9]{0,15}$".toRegex())) {
                    loginWindowState.account = it
                    loginWindowState.invalidInputAccount = false
                } else {
                    loginWindowState.invalidInputAccount = true
                }
            },
            modifier = Modifier
                .padding(40.dp)
                .shortcuts {
                    on(Key.Enter, callback = ::login)
                },
            isError = loginWindowState.invalidInputAccount,
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
        )

        BotLoginButton(
            onClick = ::login,
            modifier = Modifier
                .requiredHeight(100.dp)
        ) {
            Text("登录")
        }

//        if (loginWindowState.isException) {
//            Snackbar { Text(loginWindowState.exceptionPrompt) }
//            LaunchedEffect(Unit) {
//                delay(2000)
//                loginWindowState.isException = false
//            }
//        }
    }
}

@Composable
fun BotLoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = FloatingActionButton(
    onClick = onClick,
    modifier = modifier
        .aspectRatio(2f)
        .padding(24.dp),
    backgroundColor = MaterialTheme.colors.background,
    content = content
)

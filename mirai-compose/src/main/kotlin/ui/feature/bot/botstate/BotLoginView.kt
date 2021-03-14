@file:Suppress("NOTHING_TO_INLINE")

package com.youngerhousea.miraicompose.ui.feature.bot.botstate

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.shortcuts
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.theme.ResourceImage
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.HorizontalDottedProgressBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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


private class LoginWindowState(private val _onLogin: (account: Long, password: String) -> Unit) {
    var account by mutableStateOf(TextFieldValue())

    var password by mutableStateOf(TextFieldValue())

    var hasAccountError by mutableStateOf(false)

    var hasPasswordError by mutableStateOf(false)

    var errorTip by mutableStateOf("")

    var passwordVisualTransformation by mutableStateOf<VisualTransformation>(
        PasswordVisualTransformation()
    )

    var loading by mutableStateOf(false)

    suspend fun onLogin() {
        runCatching {
            loading = true
            this@LoginWindowState._onLogin(account.text.toLong(), password.text)
        }.onSuccess {
            loading = false
        }.onFailure {
            loading = false
            errorTip = when (it) {
                is WrongPasswordException -> {
                    hasPasswordError = true
                    "密码错误！"
                }
                is NumberFormatException -> {
                    hasAccountError = true
                    "账号格式错误"
                }
                is RetryLaterException -> {
                    "请稍后再试"
                }
                else -> {
                    it.printStackTrace()
                    "未知异常，请反馈"
                }
            }
            delay(3000)
            hasAccountError = false
            hasPasswordError = false
        }
    }

    fun onAccountTextChange(textFieldValue: TextFieldValue) {
        if (textFieldValue.text.matches("^[0-9]{0,15}$".toRegex())) {
            account = textFieldValue
            hasAccountError = false
        } else {
            hasAccountError = true
        }
    }

    fun onPasswordTextChange(textFieldValue: TextFieldValue) {
        password = textFieldValue
    }

    @Composable
    fun accountLabel() {
        if (hasAccountError)
            Text(errorTip)
        else
            Text("账号")
    }

    @Composable
    fun passwordLabel() {
        if (hasPasswordError)
            Text(errorTip)
        else
            Text("密码")
    }

    fun passwordTrailingIconChange() {
        passwordVisualTransformation =
            if (passwordVisualTransformation != VisualTransformation.None)
                VisualTransformation.None
            else
                PasswordVisualTransformation()
    }

}

@Composable
fun BotLoginView(onLogin: (account: Long, password: String) -> Unit) {
    val loginWindowState = remember { LoginWindowState(onLogin) }
    val scope = rememberCoroutineScope()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            ResourceImage.mirai,
            contentDescription = "Mirai",
            modifier = Modifier
                .padding(5.dp)
        )
        AccountTextField(loginWindowState, scope)
        PasswordTextField(loginWindowState, scope)
        LoginButton(loginWindowState, scope)
    }
}

@Composable
private inline fun AccountTextField(
    loginWindowState: LoginWindowState,
    scope: CoroutineScope
) = TextField(
    value = loginWindowState.account,
    onValueChange = loginWindowState::onAccountTextChange,
    modifier = Modifier
        .padding(40.dp)
        .shortcuts {
            on(Key.Enter, callback = { scope.launch { loginWindowState.onLogin() } })
        },
    label = {
        loginWindowState.accountLabel()
    },
    leadingIcon = {
        Icon(
            imageVector = Icons.Default.AccountBox,
            null
        )
    },
    isError = loginWindowState.hasAccountError,
    keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    ),
    singleLine = true
)

@Composable
private inline fun PasswordTextField(loginWindowState: LoginWindowState, scope: CoroutineScope) =
    TextField(
        value = loginWindowState.password,
        onValueChange = loginWindowState::onPasswordTextChange,
        modifier = Modifier
            .padding(40.dp)
            .shortcuts {
                on(Key.Enter, callback = { scope.launch { loginWindowState.onLogin() } })
            },
        label = { loginWindowState.passwordLabel() },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.VpnKey,
                contentDescription = null
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.RemoveRedEye,
                contentDescription = null,
                modifier = Modifier.clickable(onClick = loginWindowState::passwordTrailingIconChange)
            )
        },
        isError = loginWindowState.hasPasswordError,
        visualTransformation = loginWindowState.passwordVisualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        singleLine = true
    )

@Composable
private inline fun LoginButton(
    loginWindowState: LoginWindowState,
    scope: CoroutineScope
) = Button(
    onClick = { scope.launch { loginWindowState.onLogin() } },
    modifier = Modifier
        .requiredHeight(100.dp)
        .aspectRatio(2f)
        .padding(24.dp),
) {
    if (loginWindowState.loading)
        HorizontalDottedProgressBar()
    else
        Text("登录")
}


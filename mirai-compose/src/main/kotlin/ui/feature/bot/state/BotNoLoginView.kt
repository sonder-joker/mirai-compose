@file:Suppress("NOTHING_TO_INLINE")

package com.youngerhousea.miraicompose.ui.feature.bot.state

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import com.youngerhousea.miraicompose.utils.HorizontalDottedProgressBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.mamoe.mirai.network.RetryLaterException
import net.mamoe.mirai.network.WrongPasswordException

class BotNoLogin(
    componentContext: ComponentContext,
    private val onClick: (account: Long, password: String) -> Unit
) : ComponentContext by componentContext {

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
            this.onClick(account.text.toLong(), password.text)
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
            delay(1000)
            hasAccountError = false
            hasPasswordError = false
        }
    }

    suspend fun onAccountTextChange(textFieldValue: TextFieldValue) {
        if (textFieldValue.text.matches("^[0-9]{0,15}$".toRegex())) {
            account = textFieldValue
            hasAccountError = false
        } else {
            hasAccountError = true
            delay(1000)
            hasAccountError = false
        }
    }

    fun onPasswordTextChange(textFieldValue: TextFieldValue) {
        password = textFieldValue
    }

    val accountLabel =
        if (hasAccountError)
            errorTip
        else
            "Account"


    @Composable
    fun passwordLabel() {
        if (hasPasswordError)
            Text(errorTip)
        else
            Text("Password")
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
fun BotNoLoginUi(botNoLogin: BotNoLogin) {
    val scope = rememberCoroutineScope()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            ResourceImage.mirai,
            contentDescription = null,
            modifier = Modifier
                .padding(5.dp)
        )
        AccountTextField(botNoLogin, scope)
        PasswordTextField(botNoLogin, scope)
        LoginButton(botNoLogin, scope)
    }
}

@Composable
private inline fun AccountTextField(
    loginWindowState: BotNoLogin,
    scope: CoroutineScope
) = TextField(
    value = loginWindowState.account,
    onValueChange = {
        scope.launch {
            loginWindowState.onAccountTextChange(it)
        }
    },
    modifier = Modifier
        .padding(40.dp)
        .shortcuts {
            on(Key.Enter, callback = { scope.launch { loginWindowState.onLogin() } })
        },
    label = {
        Text(loginWindowState.accountLabel)
    },
    leadingIcon = {
        Icon(
            imageVector = Icons.Default.AccountCircle,
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
private inline fun PasswordTextField(loginWindowState: BotNoLogin, scope: CoroutineScope) =
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
    loginWindowState: BotNoLogin,
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
        Text("Login")
}


package com.youngerhousea.miraicompose.ui.feature.bot.state

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.shortcuts
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.instancekeeper.getOrCreate
import com.youngerhousea.miraicompose.theme.ResourceImage
import com.youngerhousea.miraicompose.utils.ComponentChildScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.mamoe.mirai.network.RetryLaterException
import net.mamoe.mirai.network.WrongPasswordException

class BotNoLogin(
    componentContext: ComponentContext,
    private val onClick: (account: Long, password: String) -> Unit
) : ComponentContext by componentContext {
    private val scope = instanceKeeper.getOrCreate(::ComponentChildScope)

    private var _account by mutableStateOf(TextFieldValue())

    val account get() = _account

    private var _password by mutableStateOf(TextFieldValue())

    val password get() = _password

    private var _hasAccountError by mutableStateOf(false)

    val hasAccountError get() = _hasAccountError

    private var _hasPasswordError by mutableStateOf(false)

    val hasPasswordError get() = _hasPasswordError

    var errorTip by mutableStateOf("")

    var _passwordVisualTransformation by mutableStateOf<VisualTransformation>(
        PasswordVisualTransformation()
    )

    val passwordVisualTransformation get() = _passwordVisualTransformation

    private var _loading by mutableStateOf(false)

    val loading get() = _loading

    fun onLogin() {
        scope.launch {
            runCatching {
                _loading = true
                onClick(_account.text.toLong(), _password.text)
            }.onSuccess {
                _loading = false
            }.onFailure {
                _loading = false
                errorTip = when (it) {
                    is WrongPasswordException -> {
                        _hasPasswordError = true
                        "密码错误！"
                    }
                    is NumberFormatException -> {
                        _hasAccountError = true
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
                launch {
                    delay(1000)
                    _hasAccountError = false
                    _hasPasswordError = false
                }
            }
        }

    }

    fun onAccountTextChange(textFieldValue: TextFieldValue) {
        if (textFieldValue.text.matches("^[0-9]{0,15}$".toRegex())) {
            _account = textFieldValue
            _hasAccountError = false
        } else {
            _hasAccountError = true
            scope.launch {
                delay(1000)
                _hasAccountError = false
            }
        }
    }

    fun onPasswordTextChange(textFieldValue: TextFieldValue) {
        _password = textFieldValue
    }

    val accountLabel =
        if (_hasAccountError)
            errorTip
        else
            "Account"


    @Composable
    fun passwordLabel() {
        if (_hasPasswordError)
            Text(errorTip)
        else
            Text("Password")
    }

    fun passwordTrailingIconChange() {
        _passwordVisualTransformation =
            if (_passwordVisualTransformation != VisualTransformation.None)
                VisualTransformation.None
            else
                PasswordVisualTransformation()
    }

}

@Composable
fun BotNoLoginUi(botNoLogin: BotNoLogin) {
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
        AccountTextField(botNoLogin)
        PasswordTextField(botNoLogin)
        LoginButton(botNoLogin)
    }
}

@Composable
private fun AccountTextField(
    loginWindowState: BotNoLogin,
) = TextField(
    value = loginWindowState.account,
    onValueChange = loginWindowState::onAccountTextChange,
    modifier = Modifier
        .padding(40.dp)
        .shortcuts {
            on(Key.Enter, callback = loginWindowState::onLogin)
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
private fun PasswordTextField(loginWindowState: BotNoLogin) =
    TextField(
        value = loginWindowState.password,
        onValueChange = loginWindowState::onPasswordTextChange,
        modifier = Modifier
            .padding(40.dp)
            .shortcuts {
                on(Key.Enter, callback = loginWindowState::onLogin)
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
private fun LoginButton(
    loginWindowState: BotNoLogin,
) = Button(
    onClick = loginWindowState::onLogin,
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

@Composable
private fun HorizontalDottedProgressBar() {
    val color = MaterialTheme.colors.onPrimary
    val transition = rememberInfiniteTransition()
    val state by transition.animateFloat(
        initialValue = 0f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 700,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    DrawCanvas(state = state, color = color)
}


@Composable
private fun DrawCanvas(
    state: Float,
    color: Color,
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
    ) {

        val radius = (4.dp).value
        val padding = (6.dp).value

        for (i in 1..5) {
            if (i - 1 == state.toInt()) {
                drawCircle(
                    radius = radius * 2,
                    brush = SolidColor(color),
                    center = Offset(
                        x = this.center.x + radius * 2 * (i - 3) + padding * (i - 3),
                        y = this.center.y
                    )
                )
            } else {
                drawCircle(
                    radius = radius,
                    brush = SolidColor(color),
                    center = Offset(
                        x = this.center.x + radius * 2 * (i - 3) + padding * (i - 3),
                        y = this.center.y
                    )
                )
            }
        }
    }
}

package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.shortcuts
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.instancekeeper.getOrCreate
import com.youngerhousea.miraicompose.theme.R
import com.youngerhousea.miraicompose.theme.ResourceImage
import com.youngerhousea.miraicompose.utils.ComponentScope
import kotlinx.coroutines.*
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.util.CoroutineScopeUtils.childScope
import net.mamoe.mirai.network.*

/**
 * bot的登录的界面
 */
class InitLogin(
    componentContext: ComponentContext,
    private val onClick: suspend (account: Long, password: String) -> Unit,
) : ComponentContext by componentContext {
    private val scope = instanceKeeper.getOrCreate{
        ComponentScope(MiraiConsole.childScope("InitLogin"))
    }

    private var _account by mutableStateOf(TextFieldValue())

    private var _hasPasswordError by mutableStateOf(false)

    private var _password by mutableStateOf(TextFieldValue())

    private var _hasAccountError by mutableStateOf(false)

    private var _loading by mutableStateOf(false)

    val account get() = _account

    val password get() = _password

    val hasAccountError get() = _hasAccountError

    val hasPasswordError get() = _hasPasswordError

    var errorTip by mutableStateOf("")

    val loading get() = _loading

    private lateinit var job: Job

    fun cancelLogin() {
        job.cancel("Exit")
    }

    fun onLogin() {
        _loading = true
        job = scope.launch {
            runCatching {
                withTimeout(20_000) {
                    onClick(_account.text.toLong(), _password.text)
                }
            }.onFailure {
                errorTip = when (it) {
                    is WrongPasswordException -> {
                        _hasPasswordError = true
                        R.String.wrongPassword
                    }
                    is NumberFormatException -> {
                        _hasAccountError = true
                        R.String.numberFormat
                    }
                    is RetryLaterException -> {
                        _hasPasswordError = true
                        R.String.retryLater
                    }
                    is UnsupportedSliderCaptchaException -> {
                        _hasPasswordError = true
                        R.String.unsupportedSliderCaptcha
                    }
                    is UnsupportedSMSLoginException -> {
                        _hasPasswordError = true
                        R.String.unsupportedSMSLogin
                    }
                    is NoStandardInputForCaptchaException -> {
                        _hasPasswordError = true
                        R.String.noStandardInputForCaptcha
                    }
                    is NoServerAvailableException -> {
                        _hasPasswordError = true
                        R.String.noServerAvailable
                    }
                    is IllegalArgumentException -> {
                        _hasPasswordError = true
                        R.String.passwordLengthMuch
                    }
                    is TimeoutCancellationException -> {
                        _hasPasswordError = true
                        R.String.loginTimeOut
                    }
                    is CancellationException -> {
                        _hasPasswordError = true
                        R.String.loginDismiss
                    }
                    else -> throw it
                }
                scope.launch {
                    delay(1_000)
                    _hasAccountError = false
                    _hasPasswordError = false
                }
            }
            _loading = false
        }
    }

    fun onAccountTextChange(textFieldValue: TextFieldValue) {
        if (textFieldValue.text.matches("^[0-9]{0,15}$".toRegex())) {
            _account = textFieldValue
            _hasAccountError = false
        } else {
            _hasAccountError = true
            scope.launch {
                delay(1_000)
                _hasAccountError = false
            }
        }
    }

    fun onPasswordTextChange(textFieldValue: TextFieldValue) {
        _password = textFieldValue
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun InitLoginUi(initLogin: InitLogin) {
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
        AccountTextField(
            account = initLogin.account,
            onAccountTextChange = initLogin::onAccountTextChange,
            isError = initLogin.hasAccountError,
            errorLabel = initLogin.errorTip,
            onKeyEnter = initLogin::onLogin
        )
        PasswordTextField(
            password = initLogin.password,
            onPasswordTextChange = initLogin::onPasswordTextChange,
            isError = initLogin.hasPasswordError,
            errorLabel = initLogin.errorTip,
            onKeyEnter = initLogin::onLogin
        )
        LoginButton(
            onClick = initLogin::onLogin,
            isLoading = initLogin.loading
        )
        AnimatedVisibility(initLogin.loading) {
            Snackbar(action = {
                TextButton(onClick = {
                    initLogin.cancelLogin()
                }) {
                    Text("Cancel")
                }
            }) {
                Text("Loading")
            }
        }
    }
}

@Composable
private fun AccountTextField(
    account: TextFieldValue,
    onAccountTextChange: (TextFieldValue) -> Unit,
    isError: Boolean,
    errorLabel: String,
    onKeyEnter: () -> Unit
) {
    OutlinedTextField(
        value = account,
        onValueChange = onAccountTextChange,
        modifier = Modifier
            .padding(40.dp)
            .shortcuts {
                on(Key.Enter, callback = onKeyEnter)
            },
        label = {
            Text(
                if (isError)
                    errorLabel
                else
                    "Account"
            )
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.AccountCircle, null)
        },
        isError = isError,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        singleLine = true
    )
}

@Composable
private fun PasswordTextField(
    password: TextFieldValue,
    onPasswordTextChange: (TextFieldValue) -> Unit,
    isError: Boolean,
    errorLabel: String,
    onKeyEnter: () -> Unit
) {
    var passwordVisualTransformation: VisualTransformation by remember {
        mutableStateOf(
            PasswordVisualTransformation()
        )
    }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordTextChange,
        modifier = Modifier
            .padding(40.dp)
            .shortcuts {
                on(Key.Enter, callback = onKeyEnter)
            },
        label = {
            Text(
                if (isError)
                    errorLabel
                else
                    "Password"
            )
        },
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
                modifier = Modifier.clickable {
                    passwordVisualTransformation =
                        if (passwordVisualTransformation != VisualTransformation.None)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation()
                }
            )
        },
        isError = isError,
        visualTransformation = passwordVisualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        singleLine = true
    )
}

@Composable
private fun LoginButton(
    onClick: () -> Unit,
    isLoading: Boolean
) = Button(
    onClick = onClick,
    modifier = Modifier
        .requiredHeight(100.dp)
        .aspectRatio(2f)
        .padding(24.dp),
) {
    if (isLoading)
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
                    brush = SolidColor(value = color),
                    center = Offset(
                        x = center.x + radius * 2 * (i - 3) + padding * (i - 3),
                        y = center.y
                    )
                )
            } else {
                drawCircle(
                    radius = radius,
                    brush = SolidColor(value = color),
                    center = Offset(
                        x = center.x + radius * 2 * (i - 3) + padding * (i - 3),
                        y = center.y
                    )
                )
            }
        }
    }
}



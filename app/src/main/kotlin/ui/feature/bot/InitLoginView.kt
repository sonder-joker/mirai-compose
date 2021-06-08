package com.youngerhousea.miraicompose.ui.feature.bot

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
import com.youngerhousea.miraicompose.theme.R
import com.youngerhousea.miraicompose.theme.ResourceImage
import com.youngerhousea.miraicompose.utils.componentScope
import kotlinx.coroutines.*
import net.mamoe.mirai.network.*

/**
 * bot的登录的界面
 */
class InitLogin(
    componentContext: ComponentContext,
    private val onClick: suspend (account: Long, password: String) -> Unit,
) : ComponentContext by componentContext {
    private val scope = componentScope()

    private var _account by mutableStateOf(TextFieldValue())

    private var _password by mutableStateOf(TextFieldValue())

    private var _isLoading by mutableStateOf(false)

    val account get() = _account

    val password get() = _password

    val isLoading get() = _isLoading

    val state = SnackbarHostState()

    fun onLogin() {
        _isLoading = true
        scope.launch {
            runCatching {
                withTimeout(20_000) {
                    onClick(_account.text.toLong(), _password.text)
                }
                if (state.showSnackbar("Loading", "Cancel") == SnackbarResult.ActionPerformed)
                    cancel()
            }.onFailure {
                val snackBarText = when (it) {
                    is WrongPasswordException -> {
                        R.String.wrongPassword
                    }
                    is RetryLaterException -> {
                        R.String.retryLater
                    }
                    is UnsupportedSliderCaptchaException -> {
                        R.String.unsupportedSliderCaptcha
                    }
                    is UnsupportedSMSLoginException -> {
                        R.String.unsupportedSMSLogin
                    }
                    is NoStandardInputForCaptchaException -> {
                        R.String.noStandardInputForCaptcha
                    }
                    is NoServerAvailableException -> {
                        R.String.noServerAvailable
                    }
                    is IllegalArgumentException -> {
                        R.String.passwordLengthMuch
                    }
                    is TimeoutCancellationException -> {
                        R.String.loginTimeOut
                    }
                    is CancellationException -> {
                        R.String.loginDismiss
                    }
                    else -> throw it
                }
                state.showSnackbar(snackBarText)
            }
        }.invokeOnCompletion {
            _isLoading = false
        }
    }

    fun onAccountTextChange(textFieldValue: TextFieldValue) {
        _account = textFieldValue
    }

    fun onPasswordTextChange(textFieldValue: TextFieldValue) {
        _password = textFieldValue
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun InitLoginUi(initLogin: InitLogin) {
    Scaffold(scaffoldState = rememberScaffoldState(snackbarHostState = initLogin.state)) {
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
                onKeyEnter = initLogin::onLogin
            )
            PasswordTextField(
                password = initLogin.password,
                onPasswordTextChange = initLogin::onPasswordTextChange,
                onKeyEnter = initLogin::onLogin
            )
            LoginButton(
                onClick = initLogin::onLogin,
                isLoading = initLogin.isLoading
            )
        }
    }

}


@Composable
private fun AccountTextField(
    account: TextFieldValue,
    onAccountTextChange: (TextFieldValue) -> Unit,
    onKeyEnter: () -> Unit
) {
    var isError by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = account,
        onValueChange = {
            isError = !it.text.matches("^[0-9]{0,15}$".toRegex())
            onAccountTextChange(it)
        },
        modifier = Modifier
            .padding(40.dp)
            .shortcuts {
                on(Key.Enter, callback = onKeyEnter)
            },
        label = { Text("Account") },
        leadingIcon = { Icon(Icons.Default.AccountCircle, null) },
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
    onKeyEnter: () -> Unit
) {
    var passwordVisualTransformation: VisualTransformation by remember { mutableStateOf(PasswordVisualTransformation()) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordTextChange,
        modifier = Modifier
            .padding(40.dp)
            .shortcuts {
                on(Key.Enter, callback = onKeyEnter)
            },
        label = { Text("Password") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.VpnKey,
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(onClick = {
                passwordVisualTransformation =
                    if (passwordVisualTransformation != VisualTransformation.None)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation()
            }){
                Icon(
                    imageVector = Icons.Default.RemoveRedEye,
                    contentDescription = null
                )
            }
        },
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


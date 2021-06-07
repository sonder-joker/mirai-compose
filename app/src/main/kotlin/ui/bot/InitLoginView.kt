package com.youngerhousea.miraicompose.app.ui.bot

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import com.youngerhousea.miraicompose.core.component.bot.InitLogin
import com.youngerhousea.miraicompose.app.utils.R
import com.youngerhousea.miraicompose.app.utils.ResourceImage
import kotlinx.coroutines.*
import net.mamoe.mirai.network.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun InitLoginUi(initLogin: InitLogin) {
    val scope = rememberCoroutineScope()

    val (account, setAccount) = remember { mutableStateOf(TextFieldValue()) }

    val (password, setPassword) = remember { mutableStateOf(TextFieldValue()) }

    var isLoading by remember { mutableStateOf(false) }

    val state = remember { SnackbarHostState() }

    val onLogin: () -> Unit = {
        scope.launch {
            runCatching {
                withTimeout(20_000) {
                    initLogin.onLogin(account.text.toLong(), password.text)
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
            isLoading = false
        }
    }

    Scaffold(scaffoldState = rememberScaffoldState(snackbarHostState = state)) {
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
                account = account,
                onAccountTextChange = setAccount,
                onKeyEnter = onLogin
            )
            PasswordTextField(
                password = password,
                onPasswordTextChange = setPassword,
                onKeyEnter = onLogin
            )
            LoginButton(
                onClick = onLogin,
                isLoading = isLoading
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
            }) {
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



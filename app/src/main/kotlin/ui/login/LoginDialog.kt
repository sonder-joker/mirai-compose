package com.youngerhousea.mirai.compose.ui.login

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
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.youngerhousea.mirai.compose.MiraiComposeDialog
import com.youngerhousea.mirai.compose.console.LoginSolverState
import com.youngerhousea.mirai.compose.console.viewModel
import com.youngerhousea.mirai.compose.resource.R
import com.youngerhousea.mirai.compose.ui.log.onPreviewEnterDown
import com.youngerhousea.mirai.compose.viewmodel.Login
import com.youngerhousea.mirai.compose.viewmodel.LoginAction
import com.youngerhousea.mirai.compose.viewmodel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.IOException


@Composable
fun LoginDialog(loginViewModel: Login = viewModel { LoginViewModel() }) {
    val data by loginViewModel.state

    if (data.isOpen)
        MiraiComposeDialog(onCloseRequest = {
            loginViewModel.dispatch(LoginAction.Close)
        }) {
            when (val solver = data.solver) {
                LoginSolverState.Nothing -> {
                    LoginInterface(isLoading = data.isLoading, data.host) { account, password ->
                        loginViewModel.dispatch(LoginAction.TryLogin(account, password))
                    }
                }
                is LoginSolverState.PicCaptcha -> {
                    LoginSolverContent(
                        title = "Bot:${solver.bot.id}",
                        tip = "处理图片验证码",
                        load = { loadImageBitmap(ByteArrayInputStream(solver.data)) },
                        value = data.captcha,
                        onValueChange = { loginViewModel.dispatch(LoginAction.Update(it)) },
                        onFinish = {
                            loginViewModel.dispatch(LoginAction.Solve)
                        },
                        refresh = {
                            loginViewModel.dispatch(LoginAction.Refresh)
                        },
                        exit = {
                            loginViewModel.dispatch(LoginAction.Exit)
                        }
                    )

                }
                is LoginSolverState.SliderCaptcha -> {
                    LoginSolverContent(
                        title = "Bot:${solver.bot.id}",
                        tip = "处理滑动验证码",
                        value = data.slider,
                        onValueChange = { loginViewModel.dispatch(LoginAction.Update(it)) },
                        load = {
                            QRCodeImage(solver.url, qrCodeHeight = 200, qrCodeWidth = 200)
                        },
                        onFinish = {
                            loginViewModel.dispatch(LoginAction.Solve)
                        },
                        refresh = {
                            loginViewModel.dispatch(LoginAction.Refresh)
                        },
                        exit = {
                            loginViewModel.dispatch(LoginAction.Exit)
                        }
                    )
                }
                is LoginSolverState.UnsafeDevice -> {
                    LoginSolverContent(
                        title = "Bot:${solver.bot.id}",
                        tip = "处理Unsafe验证码",
                        value = data.unsafe,
                        onValueChange = { loginViewModel.dispatch(LoginAction.Update(it)) },
                        load = { Text(solver.url) },
                        onFinish = {
                            loginViewModel.dispatch(LoginAction.Solve)
                        },
                        refresh = {
                            loginViewModel.dispatch(LoginAction.Refresh)
                        },
                        exit = {
                            loginViewModel.dispatch(LoginAction.Exit)
                        }
                    )
                }
            }
        }
}

@Composable
fun LoginInterface(
    isLoading: Boolean,
    snackbarHostState: SnackbarHostState,
    onLogin: (String, String) -> Unit
) {
    val (account, setAccount) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
        modifier = Modifier.onPreviewEnterDown {
            onLogin(account, password)
        }) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                R.Image.Mirai,
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            )
            AccountTextField(
                modifier = Modifier.weight(1f),
                account = account,
                onAccountTextChange = setAccount,
            )
            PasswordTextField(
                modifier = Modifier.weight(1f),
                password = password,
                onPasswordTextChange = setPassword,
            )
            LoginButton(
                modifier = Modifier.height(100.dp)
                    .aspectRatio(2f)
                    .padding(24.dp),
                onClick = { onLogin(account, password) },
                isLoading = isLoading
            )
        }
    }
}


@Composable
private fun AccountTextField(
    modifier: Modifier = Modifier,
    account: String,
    onAccountTextChange: (String) -> Unit,
) {
    var isError by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = account,
        onValueChange = {
            isError = !it.matches("^[0-9]{0,15}$".toRegex())
            onAccountTextChange(it)
        },
        modifier = modifier,
        label = { Text(R.String.Login) },
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
    modifier: Modifier = Modifier,
    password: String,
    onPasswordTextChange: (String) -> Unit,
) {
    var passwordVisualTransformation: VisualTransformation by remember { mutableStateOf(PasswordVisualTransformation()) }

    OutlinedTextField(
        value = password,
        onValueChange = {
            onPasswordTextChange(it)
        },
        modifier = modifier,
        label = { Text(R.String.Password) },
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
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isLoading: Boolean
) = Button(
    onClick = onClick,
    modifier = modifier
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


@Composable
fun QRCodeImage(
    qrCodeData: String, qrCodeHeight: Int, qrCodeWidth: Int
) {
    AsyncImage(
        load = {
            val matrix = MultiFormatWriter().encode(
                qrCodeData,
                BarcodeFormat.QR_CODE, qrCodeWidth, qrCodeHeight
            )
            return@AsyncImage MatrixToImageWriter.toBufferedImage(matrix).toComposeImageBitmap()
        },
        painterFor = { remember { BitmapPainter(it) } },
        contentDescription = null,
        modifier = Modifier.size(200.dp)
    )
}

@Composable
fun <T> AsyncImage(
    load: suspend () -> T,
    painterFor: @Composable (T) -> Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val image: T? by produceState<T?>(null) {
        value = withContext(Dispatchers.IO) {
            try {
                load()
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    if (image != null) {
        Image(
            painter = painterFor(image!!),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    }
}

package com.youngerhousea.mirai.compose.ui.login

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.youngerhousea.mirai.compose.console.UICannotFinish
import com.youngerhousea.mirai.compose.resource.R
import com.youngerhousea.mirai.compose.ui.log.onPreviewEnterDown
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.network.*
import net.mamoe.mirai.utils.BotConfiguration
import java.io.IOException

@Composable
fun LoginInterface() {
    var account by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var protocol by remember { mutableStateOf(BotConfiguration.MiraiProtocol.ANDROID_PHONE) }
    val host = remember { SnackbarHostState() }
    var loadingState by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun onLogin() {
        scope.launch {
            val bot = MiraiConsole.addBot(account.toLong(), password) {
                this.protocol = protocol
            }
            try {
                loadingState = true
                bot.login()
            } catch (e: Exception) {
                when (e) {
                    is WrongPasswordException ->
                        host.showSnackbar(e.message ?: R.String.PasswordError)

                    is RetryLaterException -> {
                        host.showSnackbar(e.message ?: R.String.RetryLater)
                    }
                    is UnsupportedSliderCaptchaException -> {
                        host.showSnackbar(e.message ?: R.String.NotSupportSlider)
                    }
                    is UnsupportedSMSLoginException -> {
                        host.showSnackbar(e.message ?: R.String.SMSLoginError)
                    }
                    is NoStandardInputForCaptchaException -> {
                        host.showSnackbar(e.message ?: R.String.NoStandInput)
                    }
                    is NoServerAvailableException -> {
                        host.showSnackbar(e.message ?: R.String.NoServerError)
                    }
                    is IllegalArgumentException -> {
                        host.showSnackbar(e.message ?: R.String.IllPassword)
                    }
                    is CancellationException -> {
                        host.showSnackbar(e.message ?: R.String.CancelLogin)
                    }
                    is UICannotFinish -> {

                    }
                    else -> {
                        host.showSnackbar(e.message ?: R.String.UnknownError)
                    }
                }
                bot.close()
            } finally {
                loadingState = false
            }
        }
    }

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = host),
        modifier = Modifier.onPreviewEnterDown {
            onLogin()
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
                onAccountTextChange = { account = it },
            )
            PasswordTextField(
                modifier = Modifier.weight(1f),
                password = password,
                onPasswordTextChange = { password = it },
            )

            CheckProtocol(modifier = Modifier.weight(1f), protocol) {
                protocol = it
            }

            LoginButton(
                modifier = Modifier.height(100.dp)
                    .aspectRatio(2f)
                    .padding(24.dp),
                onClick = ::onLogin,
                isLoading = loadingState
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
private fun CheckProtocol(
    modifier: Modifier,
    protocol: BotConfiguration.MiraiProtocol,
    onProtocolChange: (BotConfiguration.MiraiProtocol) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val suggestions = enumValues<BotConfiguration.MiraiProtocol>()

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown

    Column(modifier = modifier.padding(vertical = 5.dp)) {
        OutlinedTextField(
            value = protocol.name,
            onValueChange = { onProtocolChange(enumValueOf(it)) },
            modifier = Modifier
                .fillMaxWidth(fraction = 0.4f)
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textFieldSize = coordinates.size.toSize()
                },
            readOnly = true,
            trailingIcon = {
                Icon(icon, null,
                    Modifier.clickable { expanded = !expanded })
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            suggestions.forEach { label ->
                DropdownMenuItem(onClick = {
                    onProtocolChange(label)
                    expanded = false
                }) {
                    Text(text = label.name)
                }
            }
        }
    }
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

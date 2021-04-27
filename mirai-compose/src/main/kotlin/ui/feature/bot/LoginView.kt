package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.Router
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.ComponentChildScope
import com.youngerhousea.miraicompose.utils.SkiaImageDecode
import com.youngerhousea.miraicompose.utils.asComponent
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.network.LoginFailedException
import net.mamoe.mirai.utils.LoginSolver
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 登录界面
 *
 * TODO: @property isExpand 是否展开
 *
 * @see InitLogin
 * @see SolvePicCaptcha
 * @see SolveSliderCaptcha
 * @see SolveUnsafeDeviceLoginVerify
 */
class Login(
    componentContext: ComponentContext,
    onLoginSuccess: (bot: Bot) -> Unit,
) : LoginSolver(), ComponentContext by componentContext {
    private val scope = ComponentChildScope()

    private val onLoginSuccess: (bot: Bot) -> Unit = {
        router.push(Configuration.InitLogin)
        onLoginSuccess(it)
    }

    private var _isExpand by mutableStateOf(false)

    private val router: Router<Configuration, Component> = router(
        initialConfiguration = Configuration.InitLogin,
        key = "EmptyBot",
        handleBackButton = true,
        childFactory = { configuration: Configuration, componentContext ->
            when (configuration) {
                is Configuration.InitLogin ->
                    InitLogin(componentContext, onClick = ::startLogin).asComponent { InitLoginUi(it) }
                is Configuration.SolvePicCaptcha ->
                    //TODO:简化参数
                    SolvePicCaptcha(
                        componentContext,
                        configuration.bot,
                        configuration.imageBitmap,
                        configuration.onSuccess
                    ).asComponent { SolvePicCaptchaUi(it) }
                is Configuration.SolveSliderCaptcha ->
                    SolveSliderCaptcha(
                        componentContext,
                        configuration.bot,
                        configuration.url,
                        configuration.result
                    ).asComponent { SolveSliderCaptchaUi(it) }
                is Configuration.SolveUnsafeDeviceLoginVerify ->
                    SolveUnsafeDeviceLoginVerify(
                        componentContext,
                        configuration.bot,
                        configuration.url,
                        configuration.result
                    ).asComponent { SolveUnsafeDeviceLoginVerifyUi(it) }
            }
        }
    )

    val isExpand get() = _isExpand

    val state get() = router.state

    private fun onExitHappened() {
        _isExpand = true
        router.push(Configuration.InitLogin)
    }

    override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? =
        suspendCoroutine { continuation ->
            router.push(Configuration.SolvePicCaptcha(bot, SkiaImageDecode(data)) { string, exception ->
                if (exception != null) {
                    router.push(Configuration.InitLogin)
                    continuation.resumeWithException(exception)
                } else {
                    continuation.resume(string)
                }
            })
        }

    override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? =
        suspendCoroutine { continuation ->
            router.push(Configuration.SolveSliderCaptcha(bot, url) { string, exception ->
                if (exception != null) {
                    router.push(Configuration.InitLogin)
                    continuation.resumeWithException(exception)
                } else {
                    continuation.resume(string)
                }
            })
        }


    override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? =
        suspendCoroutine { continuation ->
            router.push(Configuration.SolveUnsafeDeviceLoginVerify(bot, url) { string, exception ->
                if (exception != null) {
                    router.push(Configuration.InitLogin)
                    continuation.resumeWithException(exception)
                } else {
                    continuation.resume(string)
                }
            })
        }

    private fun startLogin(account: Long, password: String) {
        scope.launch {
            runCatching {
                MiraiConsole.addBot(
                    id = account,
                    password = password
                ) {
                    loginSolver = this@Login
                }.alsoLogin()
            }.onSuccess {
                onLoginSuccess(it)
            }.onFailure {
                if (it is LoginFailedException)
                //TODO: 异常提示
                    onExitHappened()
                else
                    throw it
            }
        }
    }

    fun setIsExpand(isExpand: Boolean) {
        _isExpand = isExpand
    }

    sealed class Configuration : Parcelable {
        object InitLogin : Configuration()
        class SolvePicCaptcha(
            val bot: Bot,
            val imageBitmap: ImageBitmap,
            val onSuccess: (String?, ReturnException?) -> Unit
        ) : Configuration()

        class SolveSliderCaptcha(
            val bot: Bot,
            val url: String,
            val result: (String?, ReturnException?) -> Unit
        ) : Configuration()

        class SolveUnsafeDeviceLoginVerify(
            val bot: Bot,
            val url: String,
            val result: (String?, ReturnException?) -> Unit
        ) : Configuration()
    }
}

@Composable
fun LoginUi(login: Login) {
    HorizontalNotification(isExpand = login.isExpand, login::setIsExpand, "Error", Color.Red, Color.White)
    VerticalNotification(isExpand = login.isExpand, login::setIsExpand, "Login Failure")
    Children(login.state) { child ->
        child.instance()
    }
}

//TODO: 简化函数
@Composable
fun VerticalNotification(
    isExpand: Boolean,
    setIsExpand: (Boolean) -> Unit,
    text: String,
    backgroundColor: Color = Color.White,
    textColor: Color = MaterialTheme.colors.error
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        BoxWithConstraints(modifier = Modifier.padding(end = 10.dp)) {
            DropdownMenu(
                isExpand,
                onDismissRequest = { /*setIsExpand(false)*/ },
                modifier = Modifier.background(backgroundColor).drawBehind {
                    drawLine(
                        color = textColor,
                        start = Offset(18f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 10F
                    )
                    drawLine(
                        color = textColor,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = 17F
                    )
                }
            ) {
                DropdownMenuItem(onClick = { setIsExpand(false) }) {
                    // TODO better style
                    Column(verticalArrangement = Arrangement.Top, modifier = Modifier.fillMaxSize()) {
                        Row(modifier = Modifier.padding(top = 5.dp)) {
                            Text(
                                text = "Error",
                                color = textColor,
                                modifier = Modifier.padding(10.dp),
                                style = TextStyle.Default.copy(
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Left
                                )
                            )
                            Text(
                                text = "X",
                                color = textColor.copy(alpha = 0.7F),
                                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                                style = TextStyle.Default.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    textAlign = TextAlign.Right
                                )
                            )
                        }
                        Row {
                            Text(
                                text = text,
                                color = textColor,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

//TODO: 简化函数
@Composable
fun HorizontalNotification(
    isExpand: Boolean,
    setIsExpand: (Boolean) -> Unit,
    text: String,
    backgroundColor: Color = MaterialTheme.colors.error,
    textColor: Color = Color.White
) {
    BoxWithConstraints(
        modifier = Modifier
            .clipToBounds()
    ) {
        if (isExpand) {
            Snackbar(
                action = {
                    Text(
                        modifier = Modifier.background(color = backgroundColor)
                            .clickable {
                                setIsExpand(false)
                            },
                        text = "X",
                        color = textColor
                    )
                },
                backgroundColor = backgroundColor
            ) {
                Text(text = text, modifier = Modifier.background(color = backgroundColor), color = textColor)
            }
        }
    }
}




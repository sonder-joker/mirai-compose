package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
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
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.network.LoginFailedException
import net.mamoe.mirai.utils.LoginSolver
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface LoginI {

}

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
                }
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
    VerticalNotification(isExpand = login.isExpand, login::setIsExpand, "Error", Color.Red, Color.White)
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
    backgroundColor: Color,
    textColor: Color
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        BoxWithConstraints {
            DropdownMenu(
                isExpand,
                onDismissRequest = { setIsExpand(false) },
                modifier = Modifier
                    .background(backgroundColor)
            ) {
                DropdownMenuItem(onClick = { setIsExpand(false) }) {
                    Text(text = text, color = textColor)
                    // TODO better style
                    Text(text = "X", color = Color.White)
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
    backgroundColor: Color,
    textColor: Color
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



package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.runtime.Composable
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

class Login(
    componentContext: ComponentContext,
    onLoginSuccess: (bot: Bot) -> Unit,
) : LoginSolver(), ComponentContext by componentContext {
    private val scope = ComponentChildScope()

    private val onLoginSuccess: (bot: Bot) -> Unit = {
        router.push(BotStatus.NoLogin)
        onLoginSuccess(it)
    }


    sealed class BotStatus : Parcelable {
        object NoLogin : BotStatus()
        class SolvePicCaptcha(
            val bot: Bot,
            val imageBitmap: ImageBitmap,
            val onSuccess: (String?, ReturnException?) -> Unit
        ) : BotStatus()

        class SolveSliderCaptcha(
            val bot: Bot,
            val url: String,
            val result: (String?, ReturnException?) -> Unit
        ) : BotStatus()

        class SolveUnsafeDeviceLoginVerify(
            val bot: Bot,
            val url: String,
            val result: (String?, ReturnException?) -> Unit
        ) : BotStatus()
    }

    private val router: Router<BotStatus, Component> = router(
        initialConfiguration = BotStatus.NoLogin,
        key = "EmptyBot",
        handleBackButton = true,
        childFactory = { configuration: BotStatus, componentContext ->
            return@router when (configuration) {
                is BotStatus.NoLogin ->
                    BotNoLogin(componentContext, onClick = ::onClick)
                        .asComponent { BotNoLoginUi(it) }
                is BotStatus.SolvePicCaptcha ->
                    BotSolvePicCaptcha(
                        componentContext,
                        configuration.bot,
                        configuration.imageBitmap,
                        configuration.onSuccess
                    ).asComponent { BotSolvePicCaptchaUi(it) }
                is BotStatus.SolveSliderCaptcha ->
                    BotSolveSliderCaptcha(
                        componentContext,
                        configuration.bot,
                        configuration.url,
                        configuration.result
                    ).asComponent { BotSolveSliderCaptchaUi(it) }
                is BotStatus.SolveUnsafeDeviceLoginVerify ->
                    BotSolveUnsafeDeviceLoginVerify(
                        componentContext,
                        configuration.bot,
                        configuration.url,
                        configuration.result
                    ).asComponent { BotSolveUnsafeDeviceLoginVerifyUi(it) }
            }
        }
    )

    private fun onExitHappened() {
        router.push(BotStatus.NoLogin)
    }

    val state get() = router.state

    private fun onClick(account: Long, password: String) {
        scope.launch {
            runCatching {
                MiraiConsole.addBot(
                    id = account,
                    password = password
                ) {
                    loginSolver = this@Login
                }
            }.onSuccess(onLoginSuccess).onFailure {
                if (it is LoginFailedException)
                //TODO: 异常提示
                    onExitHappened()
                else
                    throw it
            }
        }
    }

    override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? =
        suspendCoroutine { continuation ->
            router.push(BotStatus.SolvePicCaptcha(bot, SkiaImageDecode(data)) { string, exception ->
                if (exception != null) {
                    router.push(BotStatus.NoLogin)
                    continuation.resumeWithException(exception)
                } else {
                    continuation.resume(string)
                }
            })
        }

    override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? =
        suspendCoroutine { continuation ->
            router.push(BotStatus.SolveSliderCaptcha(bot, url) { string, exception ->
                if (exception != null) {
                    router.push(BotStatus.NoLogin)
                    continuation.resumeWithException(exception)
                } else {
                    continuation.resume(string)
                }
            })
        }


    override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? =
        suspendCoroutine { continuation ->
            router.push(BotStatus.SolveUnsafeDeviceLoginVerify(bot, url) { string, exception ->
                if (exception != null) {
                    router.push(BotStatus.NoLogin)
                    continuation.resumeWithException(exception)
                } else {
                    continuation.resume(string)
                }
            })
        }
}

@Composable
fun LoginUi(login: Login) {
    Children(login.state) { child ->
        child.instance()
    }
}





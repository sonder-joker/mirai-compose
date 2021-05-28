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
import com.youngerhousea.miraicompose.utils.SkiaImageDecode
import com.youngerhousea.miraicompose.utils.asComponent
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
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
    private val onLoginSuccess: (bot: Bot) -> Unit,
) : LoginSolver(), ComponentContext by componentContext {

    private val router: Router<Configuration, Component> = router(
        initialConfiguration = Configuration.InitLogin,
        key = "EmptyBot",
        handleBackButton = true,
        childFactory = { configuration: Configuration, componentContext ->
            when (configuration) {
                is Configuration.InitLogin ->
                    InitLogin(
                        componentContext,
                        onClick = ::startLogin
                    ).asComponent { InitLoginUi(it) }
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


    val state get() = router.state

    private fun onExitHappened() {
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

    private suspend fun startLogin(account: Long, password: String) {
        runCatching {
            MiraiConsole.addBot(
                id = account,
                password = password
            ) {
                loginSolver = this@Login
            }.alsoLogin()
        }.onSuccess(
            onLoginSuccess
        ).onFailure {
            onExitHappened()
            throw it
        }
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
    Children(login.state) { child ->
        child.instance()
    }
}





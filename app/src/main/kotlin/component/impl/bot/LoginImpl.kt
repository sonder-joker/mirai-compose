package com.youngerhousea.miraicompose.component.impl.bot

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.Router
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.youngerhousea.miraicompose.component.bot.Login
import com.youngerhousea.miraicompose.ui.bot.InitLoginUi
import com.youngerhousea.miraicompose.ui.bot.SolvePicCaptchaUi
import com.youngerhousea.miraicompose.ui.bot.SolveSliderCaptchaUi
import com.youngerhousea.miraicompose.ui.bot.SolveUnsafeDeviceLoginVerifyUi
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


class LoginImpl(
    componentContext: ComponentContext,
    private val onLoginSuccess: (bot: Bot) -> Unit,
) : Login, LoginSolver(), ComponentContext by componentContext {

    private val router: Router<Login.Configuration, Component> = router(
        initialConfiguration = Login.Configuration.InitLogin,
        key = "EmptyBot",
        handleBackButton = true,
        childFactory = { configuration: Login.Configuration, componentContext ->
            when (configuration) {
                is Login.Configuration.InitLogin ->
                    InitLoginImpl(
                        componentContext,
                        onClick = ::startLogin
                    ).asComponent { InitLoginUi(it) }
                is Login.Configuration.SolvePicCaptcha ->
                    //TODO:简化参数
                    SolvePicCaptchaImpl(
                        componentContext,
                        configuration.bot,
                        configuration.imageBitmap,
                        configuration.onSuccess
                    ).asComponent { SolvePicCaptchaUi(it) }
                is Login.Configuration.SolveSliderCaptcha ->
                    SolveSliderCaptchaImpl(
                        componentContext,
                        configuration.bot,
                        configuration.url,
                        configuration.result
                    ).asComponent { SolveSliderCaptchaUi(it) }
                is Login.Configuration.SolveUnsafeDeviceLoginVerify ->
                    SolveUnsafeDeviceLoginVerifyImpl(
                        componentContext,
                        configuration.bot,
                        configuration.url,
                        configuration.result
                    ).asComponent { SolveUnsafeDeviceLoginVerifyUi(it) }
            }
        }
    )


    override val state get() = router.state

    private fun onExitHappened() {
        router.push(Login.Configuration.InitLogin)
    }

    override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? =
        suspendCoroutine { continuation ->
            router.push(Login.Configuration.SolvePicCaptcha(bot, SkiaImageDecode(data)) { string, exception ->
                if (exception != null) {
                    router.push(Login.Configuration.InitLogin)
                    continuation.resumeWithException(exception)
                } else {
                    continuation.resume(string)
                }
            })
        }

    override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? =
        suspendCoroutine { continuation ->
            router.push(Login.Configuration.SolveSliderCaptcha(bot, url) { string, exception ->
                if (exception != null) {
                    router.push(Login.Configuration.InitLogin)
                    continuation.resumeWithException(exception)
                } else {
                    continuation.resume(string)
                }
            })
        }


    override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? =
        suspendCoroutine { continuation ->
            router.push(Login.Configuration.SolveUnsafeDeviceLoginVerify(bot, url) { string, exception ->
                if (exception != null) {
                    router.push(Login.Configuration.InitLogin)
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
                loginSolver = this@LoginImpl
            }.alsoLogin()
        }.onSuccess(
            onLoginSuccess
        ).onFailure {
            onExitHappened()
            throw it
        }
    }

}
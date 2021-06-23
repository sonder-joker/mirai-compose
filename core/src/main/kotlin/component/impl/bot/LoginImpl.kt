package com.youngerhousea.miraicompose.core.component.impl.bot

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.Router
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.core.component.bot.Login
import com.youngerhousea.miraicompose.core.component.bot.ReturnException
import com.youngerhousea.miraicompose.core.console.MiraiCompose
import com.youngerhousea.miraicompose.core.utils.componentScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start
import net.mamoe.mirai.utils.LoginSolver
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


internal class LoginImpl(
    componentContext: ComponentContext,
    private val onLoginSuccess: (bot: Bot) -> Unit,
    val composeFactory: (LoginSolver) -> MiraiCompose
) : Login, LoginSolver(), ComponentContext by componentContext, CoroutineScope by componentContext.componentScope() {
    val compose = composeFactory(this)

    sealed class Configuration : Parcelable {
        object InitLogin : Configuration()
        class SolvePicCaptcha(
            val bot: Bot,
            val data: ByteArray,
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

    private val router: Router<Configuration, Login.Children> = router(
        initialConfiguration = Configuration.InitLogin,
        key = "EmptyBot",
        handleBackButton = true,
        childFactory = { configuration: Configuration, componentContext ->
            when (configuration) {
                is Configuration.InitLogin ->
                    Login.Children.CInitLogin(
                        InitLoginImpl(
                            componentContext,
                            onClick = ::startLogin
                        )
                    )
                is Configuration.SolvePicCaptcha ->
                    //TODO:简化参数
                    Login.Children.CSolvePicCaptcha(
                        SolvePicCaptchaImpl(
                            componentContext,
                            configuration.bot,
                            configuration.data,
                            configuration.onSuccess
                        )
                    )
                is Configuration.SolveSliderCaptcha ->
                    Login.Children.CSolveSliderCaptcha(
                        SolveSliderCaptchaImpl(
                            componentContext,
                            configuration.bot,
                            configuration.url,
                            configuration.result
                        )
                    )
                is Configuration.SolveUnsafeDeviceLoginVerify ->
                    Login.Children.CSolveUnsafeDeviceLoginVerify(
                        SolveUnsafeDeviceLoginVerifyImpl(
                            componentContext,
                            configuration.bot,
                            configuration.url,
                            configuration.result
                        )
                    )
            }
        }
    )

    init {
        //NOT GOOD
        launch(Dispatchers.IO) {
            while (true) {
                @Suppress("SENSELESS_COMPARISON")
                if (router != null) {
                    compose.start()
                }
                if (compose.isActive) {
                    break
                }
            }

        }
    }

    override val state get() = router.state

    private fun onExitHappened() {
        router.push(Configuration.InitLogin)
    }

    override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? =
        suspendCoroutine { continuation ->
            router.push(Configuration.SolvePicCaptcha(bot, data) { string, exception ->
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
//                        continuation.resumeWithException(ReturnException(message =))
                    continuation.resumeWithException(exception)
                } else {
                    continuation.resume(string)
                }
//                try {
//                    continuation.resume(string)
//                } catch (e: Exception) {
//                    continuation.resumeWithException(e)
//                }
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
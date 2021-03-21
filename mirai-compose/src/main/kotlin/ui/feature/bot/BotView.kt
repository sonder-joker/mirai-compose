package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.pop
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.ui.feature.bot.botstate.*
import com.youngerhousea.miraicompose.utils.Component
import kotlinx.coroutines.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.utils.LoginSolver
import kotlin.coroutines.resume

class BotV(componentContext: ComponentContext, val model: ComposeBot) : Component,
    ComponentContext by componentContext {

    private val router = router<BotState, Component>(
        initialConfiguration = when (model.state) {
            ComposeBot.State.NoLogin -> BotState.NoLogin
            ComposeBot.State.Online -> BotState.Online(model)
            ComposeBot.State.Loading -> throw Exception("Can't be in loading!")
        },
        key = model.hashCode().toString(),
        handleBackButton = true,
        componentFactory = { configuration: BotState, componentContext ->
            when (configuration) {
                is BotState.NoLogin ->
                    BotNoLogin(componentContext, onClick = ::onClick)
                is BotState.Lo ->
                    BotLo(componentContext, configuration.bot, configuration.data, configuration.result)
                is BotState.Load ->
                    BotLoad(componentContext, configuration.bot, configuration.url, configuration.result)
                is BotState.Loading ->
                    BotLoading(componentContext, configuration.bot, configuration.url, configuration.result)
                is BotState.Online ->
                    BotOnline(componentContext, configuration.bot.toBot())

            }
        }
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun onClick(account: Long, password: String) {
        MiraiConsole.launch {
            kotlin.runCatching {
                model.login(account, password) {
                    loginSolver = object : LoginSolver() {
                        private fun errorHandler(
                            continuation: CancellableContinuation<String?>,
                            bot: Bot
                        ) = continuation.invokeOnCancellation {
                            if (it != null) {
                                // pass exception?
                                bot.logger.error(it)
                                router.push(BotState.NoLogin)
                            }
                        }

                        override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? =
                            suspendCancellableCoroutine { continuation ->
                                errorHandler(continuation, bot)
                                router.push(BotState.Lo(bot, data) {
                                    continuation.resume(it)
                                })
                            }


                        override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? =
                            suspendCancellableCoroutine { continuation ->
                                errorHandler(continuation, bot)
                                router.push(BotState.Load(bot, url) {
                                    continuation.resume(it)
                                })
                            }

                        override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? =
                            suspendCancellableCoroutine { continuation ->
                                errorHandler(continuation, bot)
                                router.push(BotState.Loading(bot, url) {
                                    continuation.resume(it)
                                })
                            }
                    }
                }
            }.onSuccess {
                router.push(BotState.Online(model))
            }.onFailure {
                router.pop()
            }
        }
    }


    @Composable
    override fun render() {
        Children(router.state) { child, _ ->
            child.render()
        }
    }

    private sealed class BotState : Parcelable {
        object NoLogin : BotState()
        class Lo(val bot: Bot, val data: ByteArray, val result: suspend (String?) -> Unit) : BotState()
        class Load(val bot: Bot, val url: String, val result: suspend (String?) -> Unit) : BotState()
        class Loading(val bot: Bot, val url: String, val result: suspend (String?) -> Unit) : BotState()
        class Online(val bot: ComposeBot) : BotState()
    }

}


class Action(
    val onError: (Throwable?) -> Unit,
    val onSolvePicCaptcha: (bot: Bot, data: ByteArray) -> String?,
    val onSolveSliderCaptcha: (bot: Bot, url: String) -> String?,
    val onSolveUnsafeDeviceLoginVerify: (bot: Bot, url: String) -> String?
) : LoginSolver() {
    private fun errorHandler(
        continuation: CancellableContinuation<String?>,
        bot: Bot
    ) = continuation.invokeOnCancellation {
        if (it != null) {
            // pass exception?
            bot.logger.error(it)
            onError(it)
        }
    }

    override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? =
        suspendCancellableCoroutine { continuation ->
            errorHandler(continuation, bot)
        }


    override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? =
        suspendCancellableCoroutine { continuation ->
            errorHandler(continuation, bot)
        }

    override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? =
        suspendCancellableCoroutine { continuation ->
            errorHandler(continuation, bot)
        }
}
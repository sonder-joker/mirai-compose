package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.pop
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.ui.feature.bot.state.*
import com.youngerhousea.miraicompose.utils.asComponent
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.utils.LoginSolver
import kotlin.coroutines.resume

class BotState(
    componentContext: ComponentContext,
    val model: ComposeBot
) : ComponentContext by componentContext {

    sealed class BotStatus : Parcelable {
        object NoLogin : BotStatus()
        class SolvePicCaptcha(val bot: Bot, val data: ByteArray, val result: (String?) -> Unit) : BotStatus()
        class SolveSliderCaptcha(val bot: Bot, val url: String, val result: (String?) -> Unit) : BotStatus()
        class SolveUnsafeDeviceLoginVerify(val bot: Bot, val url: String, val result: (String?) -> Unit) : BotStatus()
        class Online(val bot: ComposeBot) : BotStatus()
    }

    private val router = router(
        initialConfiguration = when (model.state) {
            ComposeBot.State.NoLogin -> BotStatus.NoLogin
            ComposeBot.State.Online -> BotStatus.Online(model)
            ComposeBot.State.Loading -> throw Exception("Can't be in loading!")
        },
        key = model.hashCode().toString(),
        handleBackButton = true,
        childFactory = { configuration: BotStatus, componentContext ->
            when (configuration) {
                is BotStatus.NoLogin ->
                    BotNoLogin(componentContext, onClick = ::onClick)
                        .asComponent { BotNoLoginUi(it) }
                is BotStatus.SolvePicCaptcha ->
                    BotSolvePicCaptcha(
                        componentContext,
                        configuration.bot,
                        configuration.data,
                        configuration.result
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
                is BotStatus.Online ->
                    BotOnline(componentContext, configuration.bot).asComponent { BotOnlineUi(it) }
            }
        }
    )

    val state get() = router.state

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
                                router.push(BotStatus.NoLogin)
                            }
                        }

                        // 图片验证码
                        override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? =
                            suspendCancellableCoroutine { continuation ->
                                errorHandler(continuation, bot)
                                router.push(BotStatus.SolvePicCaptcha(bot, data) {
                                    continuation.resume(it)
                                })
                            }

                        // 滑动验证码
                        override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? =
                            suspendCancellableCoroutine { continuation ->
                                errorHandler(continuation, bot)
                                router.push(BotStatus.SolveSliderCaptcha(bot, url) {
                                    continuation.resume(it)
                                })
                            }

                        // 不安全设备验证
                        override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? =
                            suspendCancellableCoroutine { continuation ->
                                errorHandler(continuation, bot)
                                router.push(BotStatus.SolveUnsafeDeviceLoginVerify(bot, url) {
                                    continuation.resume(it)
                                })
                            }
                    }
                }
            }.onSuccess {
                router.push(BotStatus.Online(model))
            }.onFailure {
                router.pop()
            }
        }
    }


}

@Composable
fun BotStateUi(botState: BotState) {
    Children(botState.state) { child ->
        child.instance()
    }
}





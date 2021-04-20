package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.console.routeLogin
import com.youngerhousea.miraicompose.ui.feature.bot.state.*
import com.youngerhousea.miraicompose.utils.ComponentChildScope
import com.youngerhousea.miraicompose.utils.asComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.MiraiConsole

class BotState(
    componentContext: ComponentContext,
    val bot: Bot?,
    val onLoginSuccess: (bot: Bot) -> Unit
) : ComponentContext by componentContext {
    private val scope = ComponentChildScope()

    sealed class BotStatus : Parcelable {
        object NoLogin : BotStatus()
        class SolvePicCaptcha(val bot: Bot, val imageBitmap: ImageBitmap, val onSuccess: (String?) -> Unit) :
            BotStatus()

        class SolveSliderCaptcha(val bot: Bot, val url: String, val result: (String?) -> Unit) : BotStatus()
        class SolveUnsafeDeviceLoginVerify(val bot: Bot, val url: String, val result: (String?) -> Unit) : BotStatus()
        class Online(val bot: Bot) : BotStatus()
    }

    private val router = router(
        initialConfiguration = bot?.let { BotStatus.Online(it) } ?: BotStatus.NoLogin,
        key = bot.hashCode().toString(),
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
                is BotStatus.Online ->
                    BotOnline(componentContext, configuration.bot).asComponent { BotOnlineUi(it) }
            }
        }
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun onExitHappened(throwable: Throwable) {
        router.push(BotStatus.NoLogin)
        // better in future
        MiraiConsole.mainLogger.error(throwable)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun enterPicCaptcha(bot: Bot, image: ImageBitmap): String? =
        suspendCancellableCoroutine { continuation ->
            router.push(BotStatus.SolvePicCaptcha(bot, image) {
                continuation.resume(it, ::onExitHappened)
            })
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun enterUnsafeDevice(bot: Bot, url: String): String? =
        suspendCancellableCoroutine { continuation ->
            router.push(BotStatus.SolveUnsafeDeviceLoginVerify(bot, url) {
                continuation.resume(it, ::onExitHappened)
            })
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun enterSliderCaptcha(bot: Bot, url: String): String? =
        suspendCancellableCoroutine { continuation ->
            router.push(BotStatus.SolveSliderCaptcha(bot, url) {
                continuation.resume(it, ::onExitHappened)
            })
        }

    val state get() = router.state

    private fun onClick(account: Long, password: String) {
        require(bot != null) { "If this happened please report" }
        scope.launch {
            MiraiConsole.routeLogin(
                account = account,
                password = password,
                enterPicCaptcha = ::enterPicCaptcha,
                enterUnsafeDevice = ::enterUnsafeDevice,
                enterSliderCaptcha = ::enterSliderCaptcha,
                onLoginSuccess = { bot ->
                    onLoginSuccess(bot)
                    router.push(BotStatus.Online(bot))
                },
                onExitHappened = ::onExitHappened
            )
        }
    }

}

@Composable
fun BotStateUi(botState: BotState) {
    Children(botState.state) { child ->
        child.instance()
    }
}





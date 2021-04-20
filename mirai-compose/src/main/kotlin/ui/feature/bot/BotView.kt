package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import com.arkivanov.decompose.*
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
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
    val index: Int,
    val onLoginSuccess: (index: Int, bot: Bot) -> Unit,
) : ComponentContext by componentContext {
    private val scope = ComponentChildScope()

    sealed class BotStatus : Parcelable {
        object NoLogin : BotStatus()
        class SolvePicCaptcha(val imageBitmap: ImageBitmap, val onSuccess: (String?) -> Unit) :
            BotStatus()

        class SolveSliderCaptcha(val url: String, val result: (String?) -> Unit) : BotStatus()
        class SolveUnsafeDeviceLoginVerify(val url: String, val result: (String?) -> Unit) : BotStatus()
        object Online : BotStatus()
    }

    private val router = router(
        initialConfiguration = bot?.let { BotStatus.Online } ?: BotStatus.NoLogin,
        key = bot?.stringId ?: "EmptyBot",
        handleBackButton = true,
        childFactory = { configuration: BotStatus, componentContext ->
            return@router when (configuration) {
                is BotStatus.NoLogin ->
                    BotNoLogin(componentContext, onClick = ::onClick)
                        .asComponent { BotNoLoginUi(it) }
                is BotStatus.SolvePicCaptcha ->
                    BotSolvePicCaptcha(
                        componentContext,
                        bot!!,
                        configuration.imageBitmap,
                        configuration.onSuccess
                    ).asComponent { BotSolvePicCaptchaUi(it) }
                is BotStatus.SolveSliderCaptcha ->
                    BotSolveSliderCaptcha(
                        componentContext,
                        bot!!,
                        configuration.url,
                        configuration.result
                    ).asComponent { BotSolveSliderCaptchaUi(it) }
                is BotStatus.SolveUnsafeDeviceLoginVerify ->
                    BotSolveUnsafeDeviceLoginVerify(
                        componentContext,
                        bot!!,
                        configuration.url,
                        configuration.result
                    ).asComponent { BotSolveUnsafeDeviceLoginVerifyUi(it) }
                is BotStatus.Online ->
                    BotOnline(componentContext, bot!!).asComponent { BotOnlineUi(it) }
            }
        }
    )

    private fun onExitHappened(throwable: Throwable) {
        router.popWhile { it is BotStatus.NoLogin }
        // better in future
        MiraiConsole.mainLogger.error(throwable)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun onVerifyErrorHappened(throwable: Throwable) {
        // 0router.pop()
        // better in future
        MiraiConsole.mainLogger.error(throwable)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun enterPicCaptcha(image: ImageBitmap): String? =
        suspendCancellableCoroutine { continuation ->
            router.push(BotStatus.SolvePicCaptcha(image) {
                continuation.resume(it, ::onVerifyErrorHappened)
            })
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun enterUnsafeDevice(url: String): String? =
        suspendCancellableCoroutine { continuation ->
            router.push(BotStatus.SolveUnsafeDeviceLoginVerify(url) {
                continuation.resume(it, ::onVerifyErrorHappened)
            })
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun enterSliderCaptcha(url: String): String? =
        suspendCancellableCoroutine { continuation ->
            router.push(BotStatus.SolveSliderCaptcha(url) {
                continuation.resume(it, ::onVerifyErrorHappened)
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
                    onLoginSuccess(index, bot)
                    router.push(BotStatus.Online)
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





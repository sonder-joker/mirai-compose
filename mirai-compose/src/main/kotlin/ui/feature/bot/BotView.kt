package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.*
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.console.MiraiComposeSolver
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.ComponentChildScope
import com.youngerhousea.miraicompose.utils.asComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.MiraiConsole
import kotlin.coroutines.resumeWithException

class Login(
    componentContext: ComponentContext,
    val onLoginSuccess: (bot: Bot) -> Unit,
) : ComponentContext by componentContext {
    private val scope = ComponentChildScope()
    private var isExpand: MutableState<Boolean> = mutableStateOf(false)

    sealed class BotStatus : Parcelable {
        object NoLogin : BotStatus()
        class SolvePicCaptcha(val bot: Bot, val imageBitmap: ImageBitmap, val onSuccess: (String?) -> Unit) :
            BotStatus()

        class SolveSliderCaptcha(val bot: Bot, val url: String, val result: (String?) -> Unit) : BotStatus()
        class SolveUnsafeDeviceLoginVerify(val bot: Bot, val url: String, val result: (String?, Exception?) -> Unit) :
            BotStatus()
    }

    @Composable
    fun verticalNotification(isExpand: MutableState<Boolean>, text: String, backgrouncolor: Color, textcolor: Color) {
        BoxWithConstraints(
            modifier = Modifier
                .padding(start = 100000.dp, top = 100000.dp)
                .clipToBounds()
        ) {
            DropdownMenu(
                isExpand.value,
                onDismissRequest = { isExpand.value = false },
                modifier = Modifier
                    .background(backgrouncolor)
            ) {
                DropdownMenuItem(onClick = { isExpand.value = false }) {
                    Text(text = text, color = textcolor)
                }
            }
        }
    }

    @Composable
    fun horizontalNotification(isExpand: MutableState<Boolean>, text: String, backgrouncolor: Color, textcolor: Color) {
        val bgcolor = Modifier.background(color = backgrouncolor)
        BoxWithConstraints(
            modifier = Modifier
                .clipToBounds()
        ) {
            if (isExpand.value) {
                Snackbar(
                    action = {
                        Text(
                            modifier = bgcolor
                                .clickable {
                                    isExpand.value = !isExpand.value
                                },
                            text = "X",
                            color = textcolor
                        )
                    },
                    backgroundColor = backgrouncolor
                ) { Text(text = text, modifier = bgcolor, color = textcolor) }
            }

        }
    }

    private val router: Router<BotStatus, Component> = router(
        initialConfiguration = BotStatus.NoLogin,
        key = "EmptyBot",
        handleBackButton = true,
        childFactory = { configuration: BotStatus, componentContext ->
            return@router when (configuration) {
                is BotStatus.NoLogin ->
                    BotNoLogin(componentContext, onClick = ::onClick)
                        .asComponent {
                            horizontalNotification(isExpand = isExpand, "Error", Color.Red, Color.White)
                            verticalNotification(isExpand = isExpand, "Error", Color.Red, Color.White)
                            BotNoLoginUi(it)
                        }
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
        isExpand.value = true
        router.popWhile { it is BotStatus.NoLogin }
    }

    private fun returnToUp() {
        router.popWhile { it is BotStatus.NoLogin }
    }

    private fun onVerifyErrorHappened(throwable: Throwable) {
        // 0router.pop()
        // better in future
        MiraiConsole.mainLogger.error(throwable)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun enterPicCaptcha(bot: Bot, image: ImageBitmap): String? =
        suspendCancellableCoroutine { continuation ->
            router.push(BotStatus.SolvePicCaptcha(bot, image) {
                continuation.resume(it, ::onVerifyErrorHappened)
            })
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun enterUnsafeDevice(bot: Bot, url: String): String? =
        suspendCancellableCoroutine { continuation ->
            router.push(BotStatus.SolveUnsafeDeviceLoginVerify(bot, url) { string, exception ->
                exception?.let { continuation.resumeWithException(it) } ?: continuation.resume(
                    string,
                    ::onVerifyErrorHappened
                )
            })
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun enterSliderCaptcha(bot: Bot, url: String): String? =
        suspendCancellableCoroutine { continuation ->
            router.push(BotStatus.SolveSliderCaptcha(bot, url) {
                continuation.resume(it, ::onVerifyErrorHappened)
            })
        }

    val state get() = router.state

    private fun onClick(account: Long, password: String) {
        scope.launch {
            val bot = MiraiConsole.addBot(
                id = account,
                password = password
            ) {
                loginSolver = MiraiComposeSolver(
                    enterPicCaptcha = { bot: Bot, image: ImageBitmap -> enterPicCaptcha(bot, image) },
                    enterSliderCaptcha = { bot: Bot, url: String -> enterSliderCaptcha(bot, url) },
                    enterUnsafeDevice = { bot: Bot, url: String -> enterUnsafeDevice(bot, url) },
                )
            }
            MiraiConsole.runCatching {
                bot.login()
            }.onSuccess {
                onLoginSuccess(bot)
            }.onFailure {
                bot.logger.error(it)
                onExitHappened()
            }
        }
    }

}

@Composable
fun LoginUi(login: Login) {
    Children(login.state) { child ->
        child.instance()
    }
}





package com.youngerhousea.miraicompose.console

import androidx.compose.ui.graphics.ImageBitmap
import com.youngerhousea.miraicompose.utils.SkiaImageDecode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.utils.LoginSolver

@OptIn(ExperimentalCoroutinesApi::class)
class MiraiComposeSolver(
    val enterPicCaptcha: suspend (image: ImageBitmap) -> String?,
    val enterSliderCaptcha: suspend (url: String) -> String?,
    val enterUnsafeDevice: suspend (url: String) -> String?,
) : LoginSolver() {
    // 图片验证码
    override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? =
        enterPicCaptcha(SkiaImageDecode(data))

    // 滑动验证码
    override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? =
        enterSliderCaptcha(url)

    // 不安全设备验证
    override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? =
        enterUnsafeDevice(url)
}

suspend fun MiraiConsole.routeLogin(
    account: Long,
    password: String,
    enterPicCaptcha: suspend (image: ImageBitmap) -> String?,
    enterSliderCaptcha: suspend (url: String) -> String?,
    enterUnsafeDevice: suspend (url: String) -> String?,
    onLoginSuccess: (bot: Bot) -> Unit,
    onExitHappened: (throwable: Throwable) -> Unit
) {
    val bot = MiraiConsole.addBot(account, password) {
        loginSolver = MiraiComposeSolver(
            enterPicCaptcha,
            enterSliderCaptcha,
            enterUnsafeDevice
        )
    }
    runCatching {
        bot.login()
    }.onSuccess { onLoginSuccess(bot) }.onFailure(onExitHappened)
}
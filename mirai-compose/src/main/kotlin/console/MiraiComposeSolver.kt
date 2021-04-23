package com.youngerhousea.miraicompose.console

import androidx.compose.ui.graphics.ImageBitmap
import com.youngerhousea.miraicompose.utils.SkiaImageDecode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.mamoe.mirai.Bot
import net.mamoe.mirai.utils.LoginSolver

@OptIn(ExperimentalCoroutinesApi::class)
class MiraiComposeSolver(
    val enterPicCaptcha: suspend (bot: Bot, image: ImageBitmap) -> String?,
    val enterSliderCaptcha: suspend (bot: Bot, url: String) -> String?,
    val enterUnsafeDevice: suspend (bot: Bot, url: String) -> String?,
) : LoginSolver() {
    // 图片验证码

    override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? {
        return enterPicCaptcha(bot, SkiaImageDecode(data))
    }

    // 滑动验证码
    override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? =
        enterSliderCaptcha(bot, url)

    // 不安全设备验证
    override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? =
        enterUnsafeDevice(bot, url)
}


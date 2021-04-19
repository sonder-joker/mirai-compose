package com.youngerhousea.miraicompose.console

import kotlinx.coroutines.suspendCancellableCoroutine
import net.mamoe.mirai.Bot
import net.mamoe.mirai.utils.LoginSolver

class MiraiComposeSolver : LoginSolver() {

    // 图片验证码
    override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? =
        suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation {
                if (it != null) {
                    // pass exception?
                    bot.logger.error(it)
                }
            }
//            router.push(BotState.BotStatus.SolvePicCaptcha(bot, data) {
//                continuation.resume(it)
//            })
        }

    // 滑动验证码
    override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? =
        suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation {
                if (it != null) {
                    // pass exception?
                    bot.logger.error(it)
                }
            }
//            router.push(BotState.BotStatus.SolveSliderCaptcha(bot, url) {
//                continuation.resume(it)
//            })
        }

    // 不安全设备验证
    override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? =
        suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation {
                if (it != null) {
                    // pass exception?
                    bot.logger.error(it)
                }
            }
//            router.push(BotState.BotStatus.SolveUnsafeDeviceLoginVerify(bot, url) {
//                continuation.resume(it)
//            })
        }
}
package com.youngerhousea.miraicompose.core.component.bot

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import net.mamoe.mirai.network.CustomLoginFailedException

/**
 * 登录界面
 *
 * TODO: @property isExpand 是否展开
 *
 * @see InitLogin
 * @see SolvePicCaptcha
 * @see SolveSliderCaptcha
 * @see SolveUnsafeDeviceLoginVerify
 */
interface Login {
    val state: Value<RouterState<*, Children>>

    sealed class Children {
        class CInitLogin(val initLogin: InitLogin) : Children()
        class CSolvePicCaptcha(val solvePicCaptcha: SolvePicCaptcha) : Children()
        class CSolveSliderCaptcha(val solveSliderCaptcha: SolveSliderCaptcha) : Children()
        class CSolveUnsafeDeviceLoginVerify(val solveUnsafeDeviceLoginVerify: SolveUnsafeDeviceLoginVerify) : Children()
    }

}

class ReturnException(killBot: Boolean = true, message: String = "返回") : CustomLoginFailedException(killBot, message)

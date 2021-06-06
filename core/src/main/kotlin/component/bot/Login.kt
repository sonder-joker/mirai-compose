package com.youngerhousea.miraicompose.core.component.bot

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value
import net.mamoe.mirai.Bot
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
interface Login{
    val state: Value<RouterState<Configuration, ComponentContext>>

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
}

class ReturnException(killBot: Boolean = true, message: String = "返回") : CustomLoginFailedException(killBot, message)

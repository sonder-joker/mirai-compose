package com.youngerhousea.miraicompose.core.component.impl.bot

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.core.component.bot.Event
import com.youngerhousea.miraicompose.core.component.bot.InitLogin
import com.youngerhousea.miraicompose.core.utils.componentScope
import com.youngerhousea.miraicompose.core.utils.getValue
import com.youngerhousea.miraicompose.core.utils.setValue
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import net.mamoe.mirai.network.*


internal class InitLoginImpl(
    componentContext: ComponentContext,
    private val onClick: suspend (account: Long, password: String) -> Unit,
) : InitLogin, ComponentContext by componentContext, CoroutineScope by componentContext.componentScope() {
    lateinit var job: Job

    override val model = MutableStateFlow(InitLogin.Model())

    var delegateModel by model

    override fun onAccountChange(account: String) {
        delegateModel = delegateModel.copy(account = account)

    }

    override fun onPasswordChange(password: String) {
        delegateModel = delegateModel.copy(password = password)
    }

    override fun onLogin(account: Long, password: String) {
        job = launch {
            runCatching {
                delegateModel = delegateModel.copy(event = Event.Loading("Loading"))
                withTimeout(20_000) {
                    onClick(account, password)
                }
            }.onFailure {
                when (it) {
                    is WrongPasswordException -> {
                        delegateModel = delegateModel.copy(event = Event.Error("密码错误"))
                    }
                    is RetryLaterException -> {
                        delegateModel = delegateModel.copy(event = Event.Error("请稍后再试"))
                    }
                    is UnsupportedSliderCaptchaException -> {
                        delegateModel = delegateModel.copy(event = Event.Error("目前不支持滑动输入框"))
                    }
                    is UnsupportedSMSLoginException -> {
                        delegateModel = delegateModel.copy(event = Event.Error("Mirai暂未提供短信输入"))
                    }
                    is NoStandardInputForCaptchaException -> {
                        delegateModel = delegateModel.copy(event = Event.Error("无标准输入"))
                    }
                    is NoServerAvailableException -> {
                        delegateModel = delegateModel.copy(event = Event.Error("无可用服务器"))
                    }
                    is IllegalArgumentException -> {
                        delegateModel = delegateModel.copy(event = Event.Error("密码长度最多为16"))
                    }
                    is TimeoutCancellationException -> {
                        delegateModel = delegateModel.copy(event = Event.Error("超时"))
                    }
                    is CancellationException -> {
                        delegateModel = delegateModel.copy(event = Event.Error("取消"))
                    }
                    else -> {
                        delegateModel = delegateModel.copy(event = Event.Error("Unknown Reason"))
                        throw it
                    }
                }
            }
            delegateModel = delegateModel.copy(event = Event.Normal)
        }
    }

    override fun cancelLogin() {
        job.cancel("Normal exit")
    }

}

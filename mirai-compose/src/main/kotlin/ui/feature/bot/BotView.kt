package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.pop
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.ui.feature.bot.botstate.BotLoading
import com.youngerhousea.miraicompose.ui.feature.bot.botstate.BotNoLogin
import com.youngerhousea.miraicompose.ui.feature.bot.botstate.BotOnline
import com.youngerhousea.miraicompose.utils.Component
import kotlinx.coroutines.*
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.extensions.BotConfigurationAlterer
import net.mamoe.mirai.utils.BotConfiguration
import javax.script.ScriptEngineManager
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BotV(componentContext: ComponentContext, val model: ComposeBot) : Component,
    ComponentContext by componentContext {

    private val router = router<BotState, Component>(
        initialConfiguration = when (model.state) {
            ComposeBot.State.NoLogin -> BotState.NoLogin
            ComposeBot.State.Loading -> BotState.Loading
            ComposeBot.State.Online -> BotState.Online(model)
        },
        key = model.hashCode().toString(),
        handleBackButton = true,
        componentFactory = { configuration: BotState, componentContext ->
            when (configuration) {
                is BotState.NoLogin -> {
                    BotNoLogin(componentContext, onClick = ::onClick)
                }
                is BotState.Loading -> {
                    BotLoading(componentContext)
                }
                is BotState.Online -> {
                    BotOnline(componentContext, configuration.bot.toBot())
                }
            }
        }
    )

    private fun onClick(account: Long, password: String) {
        router.push(BotState.Loading)
        MiraiConsole.launch {
            kotlin.runCatching {
                model.login(account, password)
            }.onSuccess {
                router.push(BotState.Online(model))
            }.onFailure {
                router.pop()
            }
        }
    }


    @Composable
    override fun render() {
        Children(router.state) { child, _ ->
            child.render()
        }
    }

    private sealed class BotState : Parcelable {
        object NoLogin : BotState()
        object Loading : BotState()
        class Online(val bot: ComposeBot) : BotState()
    }

}



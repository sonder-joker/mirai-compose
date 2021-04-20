package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.utils.asComponent
import net.mamoe.mirai.Bot

class BotChoose(
    componentContext: ComponentContext,
    bot: Bot?,
    onLoginSuccess: (bot: Bot) -> Unit
) : ComponentContext by componentContext {

    sealed class RightSight : Parcelable {
        object Default : RightSight()
        class BotR(val bot: Bot?) : RightSight()
    }

    val state get() = router.state

    private val router = router(
        initialConfiguration = bot?.let { RightSight.BotR(it) } ?: RightSight.Default,
        handleBackButton = true,
        childFactory = { configuration: RightSight, componentContext: ComponentContext ->
            when (configuration) {
                is RightSight.Default -> {
                    Default.asComponent { DefaultUi() }
                }
                is RightSight.BotR -> {
                    BotState(
                        componentContext,
                        configuration.bot,
                        onLoginSuccess = onLoginSuccess
                    ).asComponent { BotStateUi(it) }
                }
            }
        }
    )
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun BotChooseUi(botChoose: BotChoose) {
    Children(botChoose.state, crossfade()) { child ->
        child.instance()
    }
}


object Default

@Composable
fun DefaultUi() {
    Column {

    }
}



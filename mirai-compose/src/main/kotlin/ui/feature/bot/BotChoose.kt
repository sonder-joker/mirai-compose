package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.utils.asComponent

class BotChoose(
    componentContext: ComponentContext,
    bot: ComposeBot?,
) : ComponentContext by componentContext {

    sealed class RightSight : Parcelable {
        object Default : RightSight()
        class Bot(val item: ComposeBot) : RightSight()
    }

    val state get() = router.state

    private val router = router(
        initialConfiguration = bot?.let { RightSight.Bot(it) } ?: RightSight.Default,
        handleBackButton = true,
        componentFactory = { configuration: RightSight, componentContext: ComponentContext ->
            when (configuration) {
                is RightSight.Default -> {
                    Default.asComponent { DefaultUi() }
                }
                is RightSight.Bot -> {
                    BotState(componentContext, configuration.item).asComponent { BotStateUi(it) }
                }
            }
        }
    )
}

@Composable
fun BotChooseUi(botChoose: BotChoose) {
    Children(botChoose.state) { child, _ ->
        child()
    }
}


object Default

@Composable
fun DefaultUi() {
    Column {

    }
}



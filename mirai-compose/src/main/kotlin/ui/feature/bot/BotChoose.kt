package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.ui.common.VerticalSplittableSimple
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent

class BotChoose(
    componentContext: ComponentContext,
    val model: MutableList<ComposeBot>,
    val onSelectedBot: (ComposeBot) -> Unit
) : ComponentContext by componentContext {

    sealed class RightSight : Parcelable {
        object Default : RightSight()
        class Bot(val item: ComposeBot) : RightSight()
    }

    fun onAddButtonClick() {
        model.add(ComposeBot())
    }

    fun onItemClick(bot: ComposeBot) {
        router.push(RightSight.Bot(bot))
        onSelectedBot(bot)
    }

    fun onItemRemove(bot: ComposeBot) {
        router.push(RightSight.Default)
        model.remove(bot)
    }

    val state get() = router.state

    private val router = router<RightSight, Component>(
        initialConfiguration = RightSight.Default,
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
    VerticalSplittableSimple(
        resizablePanelContent = {
            Column {
                TopView(
                    Modifier.padding(8.dp)
                )
                BotListView(
                    botChoose.model,
                    Modifier
                        .fillMaxSize()
                        .padding(30.dp),
                    onAddButtonClick = botChoose::onAddButtonClick,
                    onItemClick = botChoose::onItemClick,
                    onItemRemove = botChoose::onItemRemove
                )
            }
        },
        rightContent = {
            Children(botChoose.state) { child, _ ->
                child()
            }
        }
    )
}


object Default {

}

@Composable
fun DefaultUi() {
    Column {

    }
}



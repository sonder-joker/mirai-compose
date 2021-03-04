package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.*
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.model.addComposeBot
import com.youngerhousea.miraicompose.ui.feature.bot.botstate.BotLoading
import com.youngerhousea.miraicompose.ui.feature.bot.botstate.BotLogin
import com.youngerhousea.miraicompose.ui.feature.bot.botstate.BotState
import com.youngerhousea.miraicompose.ui.feature.bot.listview.BotListView
import com.youngerhousea.miraicompose.ui.feature.bot.listview.TopView
import com.youngerhousea.miraicompose.utils.Component
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.MiraiConsole
import ui.common.SplitterState
import ui.common.VerticalSplittable

class BotV(componentContext: ComponentContext, val model: SnapshotStateList<ComposeBot>) : Component,
    ComponentContext by componentContext {

    private val router = router<BotState, Component>(
        initialConfiguration = BotState.Login,
        key = "BotRouter",
        handleBackButton = true,
        componentFactory = { configuration: BotState, componentContext ->
            when (configuration) {
                is BotState.Login -> {
                    BotLogin(componentContext, onClick = ::onClick)
                }
                is BotState.Loading -> {
                    BotLoading(componentContext)
                }
                is BotState.State -> {
                    BotState(componentContext, configuration.bot)
                }
            }
        }
    )

    private fun onClick(account: Long, password: String) {
        router.push(BotState.Loading)
        val bot = MiraiConsole.addComposeBot(account, password)
        bot.launch {
            bot.login()
        }.invokeOnCompletion {
            it?.let { router.push(BotState.Login) } ?: router.push(BotState.State(bot))
        }
    }

    @Composable
    override fun render() {
        Children(router.state) { child, configuration ->
            val panelState = remember { PanelState() }
            val animatedSize = if (panelState.splitter.isResizing)
                if (panelState.isExpanded) panelState.expandedSize else panelState.collapsedSize
            else
                animateDpAsState(
                    if (panelState.isExpanded) panelState.expandedSize else panelState.collapsedSize,
                    SpringSpec(stiffness = Spring.StiffnessLow)
                ).value
            VerticalSplittable(
                Modifier
                    .fillMaxSize(),
                panelState.splitter,
                onResize = {
                    panelState.expandedSize = (panelState.expandedSize + it).coerceAtLeast(panelState.expandedSizeMin)
                }
            ) {
                ResizablePanel(
                    Modifier
                        .width(animatedSize)
                        .fillMaxHeight(),
                    panelState
                ) {
                    Column {
                        TopView(
                            Modifier
                                .padding(8.dp)
                        )
                        BotListView(
                            model,
                            Modifier
                                .fillMaxSize()
                                .padding(30.dp),
                            onButtonClick = {
                                router.push(BotState.Login)
                            },
                            onItemClick = { bot ->
                                router.push(BotState.State(bot))
                            },
                            onItemRemove = { bot ->
                                bot.close()
                                router.popWhile { it is BotState.Login }
                            }
                        )
                    }
                }
                child.render()
            }
        }
    }

    sealed class BotState : Parcelable {
        object Login : BotState()
        object Loading : BotState()
        class State(val bot: ComposeBot) : BotState()
    }

}

@Composable
private fun ResizablePanel(
    modifier: Modifier,
    state: PanelState,
    content: @Composable () -> Unit,
) {
    val alpha = animateFloatAsState(
        if (state.isExpanded) 1f else 0f,
        SpringSpec(stiffness = Spring.StiffnessLow)
    ).value

    Box(modifier) {
        Box(
            Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = alpha)
        ) {
            content()
        }
        Icon(
            if (state.isExpanded) Icons.Default.ArrowBack else Icons.Default.ArrowForward,
            contentDescription = "Arrow",
            Modifier
                .padding(top = 4.dp)
                .width(24.dp)
                .clickable {
                    state.isExpanded = !state.isExpanded
                }
                .padding(4.dp)
                .align(Alignment.TopEnd),
            tint = LocalContentColor.current,
        )
    }
}

private class PanelState {
    val collapsedSize = 24.dp
    var expandedSize by mutableStateOf(250.dp)
    val expandedSizeMin = 90.dp
    var isExpanded by mutableStateOf(true)
    val splitter = SplitterState()
}

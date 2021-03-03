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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router
import com.youngerhousea.miraicompose.model.Model
import com.youngerhousea.miraicompose.ui.feature.bot.botstate.BotChooseWindow
import com.youngerhousea.miraicompose.ui.feature.bot.botstate.BotEmptyWindow
import com.youngerhousea.miraicompose.ui.feature.bot.botstate.BotLoginView
import com.youngerhousea.miraicompose.ui.feature.bot.listview.BotListView
import com.youngerhousea.miraicompose.ui.feature.bot.listview.TopView
import com.youngerhousea.miraicompose.utils.Component
import ui.common.SplitterState
import ui.common.VerticalSplittable

class Bot(componentContext: ComponentContext, val model: Model) : Component, ComponentContext by componentContext {

//    private val router = router<BotState, Component>(
//        initialConfiguration = BotState.No(),
//        componentFactory =  { configuration:BotState, componentContext ->
//            when(configuration) {
//                is BotState.Loading -> TODO()
//                is BotState.Login -> TODO()
//                is BotState.No -> TODO()
//            }
//        }
//    )

    sealed class BotState{
        class No:BotState()
        class Loading:BotState()
        class Login:BotState()
    }

    @Composable
    override fun render() {
        BotsWindow(model)
    }
}

@Composable
fun BotsWindow(model: Model) {
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
                        .padding(30.dp)
                )
            }
        }

        if (model.currentIndex == -1) {
            BotEmptyWindow()
        } else {
            BotChooseWindow(model.currentBot)
        }
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
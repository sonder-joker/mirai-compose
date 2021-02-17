package com.youngerhousea.miraicompose.ui.bot

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
import com.youngerhousea.miraicompose.model.Model
import com.youngerhousea.miraicompose.ui.bot.botstate.BotChooseWindow
import com.youngerhousea.miraicompose.ui.bot.botstate.BotEmptyWindow
import com.youngerhousea.miraicompose.ui.bot.listview.BotListView
import com.youngerhousea.miraicompose.ui.bot.listview.TopView
import com.youngerhousea.miraicompose.utils.SplitterState
import com.youngerhousea.miraicompose.utils.VerticalSplittable

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
            panelState.expandedSize =
                (panelState.expandedSize + it).coerceAtLeast(panelState.expandedSizeMin)
        }
    ) {
        ResizablePanel(
            Modifier
                .width(animatedSize)
                .fillMaxHeight(), panelState
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



        if (model.currentIndex < 0)
            BotEmptyWindow()
        else
            BotChooseWindow(model.currentBot)

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

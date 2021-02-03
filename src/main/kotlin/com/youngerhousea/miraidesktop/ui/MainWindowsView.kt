package com.youngerhousea.miraidesktop.ui

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
import com.youngerhousea.miraidesktop.model.Model
import com.youngerhousea.miraidesktop.ui.botlistwindows.BotListView
import com.youngerhousea.miraidesktop.ui.botlistwindows.TopView
import com.youngerhousea.miraidesktop.ui.botwindows.BotWindow
import com.youngerhousea.miraidesktop.ui.botwindows.SettingWindow
import com.youngerhousea.miraidesktop.utils.SplitterState
import com.youngerhousea.miraidesktop.utils.VerticalSplittable

@Composable
fun MainWindowsView(model: Model) {
    val panelState = remember { PanelState() }

    var inBotWindow by remember { mutableStateOf(true) }

    val animatedSize = if (panelState.splitter.isResizing)
        if (panelState.isExpanded) panelState.expandedSize else panelState.collapsedSize
    else
        animateDpAsState(
            if (panelState.isExpanded) panelState.expandedSize else panelState.collapsedSize,
            SpringSpec(stiffness = Spring.StiffnessLow)
        ).value

    VerticalSplittable(
        Modifier.fillMaxSize(),
        panelState.splitter,
        onResize = {
            panelState.expandedSize =
                (panelState.expandedSize + it).coerceAtLeast(panelState.expandedSizeMin)
        }
    ) {
        ResizablePanel(Modifier
            .width(animatedSize)
            .fillMaxHeight(), panelState) {
            Column {
                TopView()
                BotListView(model)
            }
        }

        Box(Modifier
            .fillMaxSize()) {
            if (inBotWindow)
                BotWindow(model.currentBot)
            else
                SettingWindow()
            Icon(
                if (inBotWindow) Icons.Default.ArrowForward else Icons.Default.ArrowBack,
                contentDescription = "ÇÐ»»",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { inBotWindow = !inBotWindow }
            )
        }
    }
}


private class PanelState {
    val collapsedSize = 24.dp
    var expandedSize by mutableStateOf(300.dp)
    val expandedSizeMin = 90.dp
    var isExpanded by mutableStateOf(true)
    val splitter = SplitterState()
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
        Box(Modifier.fillMaxSize().graphicsLayer(alpha = alpha)) {
            content()
        }
        Icon(
            if (state.isExpanded) Icons.Default.ArrowBack else Icons.Default.ArrowForward,
            contentDescription = "Arrow",
            tint = LocalContentColor.current,
            modifier = Modifier
                .padding(top = 4.dp)
                .width(24.dp)
                .clickable {
                    state.isExpanded = !state.isExpanded
                }
                .padding(4.dp)
                .align(Alignment.TopEnd)
        )
    }
}


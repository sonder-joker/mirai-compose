package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.animation.animateContentSize
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
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.ui.common.SplitterState
import com.youngerhousea.miraicompose.ui.common.VerticalSplittable
import com.youngerhousea.miraicompose.ui.feature.bot.listview.BotListView
import com.youngerhousea.miraicompose.ui.feature.bot.listview.TopView
import com.youngerhousea.miraicompose.utils.Component

class Choose(
    componentContext: ComponentContext,
    val model: MutableList<ComposeBot>,
    val onSelectedBot: (ComposeBot) -> Unit
) : Component, ComponentContext by componentContext {

    sealed class RightSight : Parcelable {
        object Default : RightSight()
        class Bot(val item: ComposeBot) : RightSight()
    }


    private val router = router<RightSight, Component>(
        initialConfiguration = RightSight.Default,
        handleBackButton = true,
        componentFactory = { configuration: RightSight, componentContext: ComponentContext ->
            when (configuration) {
                is RightSight.Default -> {
                    Default()
                }
                is RightSight.Bot -> {
                    BotV(componentContext, configuration.item)
                }
            }
        }
    )

    @Composable
    override fun render() {
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
                Column{
                    TopView(
                        Modifier.padding(8.dp)
                    )
                    BotListView(
                        model,
                        Modifier
                            .fillMaxSize()
                            .padding(30.dp),
                        onAddButtonClick = {
                            model.add(ComposeBot())
                        },
                        onItemClick = { bot ->
                            router.push(RightSight.Bot(bot))
                            onSelectedBot(bot)
                        },
                        onItemRemove = { bot ->
                            router.push(RightSight.Default)
                            model.remove(bot)
                        }
                    )
                }
            }

            Children(router.state) { child, _ ->
                child.render()
            }
        }
    }
}

@Composable
private fun ResizablePanel(
    modifier: Modifier,
    state: PanelState,
    content: @Composable () -> Unit,
) {
    val alpha by animateFloatAsState(
        if (state.isExpanded) 1f else 0f,
        SpringSpec(stiffness = Spring.StiffnessLow)
    )

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
            contentDescription = null,
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

class Default : Component {
    @Composable
    override fun render() {
        Column {

        }
    }

}
package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.push
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.ui.common.VerticalSplittable
import com.youngerhousea.miraicompose.ui.feature.bot.listview.BotListView
import com.youngerhousea.miraicompose.ui.feature.bot.listview.TopView
import com.youngerhousea.miraicompose.utils.Component
import kotlinx.coroutines.launch

class Choose(
    componentContext: ComponentContext,
    val model: MutableList<ComposeBot>
) : Component, ComponentContext by componentContext {

    private val r = router()


    override fun render() {
        val panelState = remember { PanelState() }
        val scope = rememberCoroutineScope()
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
                            if (configuration !is BotState.Login)
                                router.push(BotState.Login)
                        },
                        onItemClick = { bot ->
                            router.push(BotState.State(bot))
                        },
                        onItemRemove = { bot ->
                            scope.launch {
                                bot.closeAndJoin()
                            }.invokeOnCompletion {
                                router.push(BotState.Login)
                            }
                        }
                    )
                }

            }
    }
}
package com.youngerhousea.miraidesktop.ui.botlistwindows

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraidesktop.model.ComposeBot
import com.youngerhousea.miraidesktop.model.Model
import com.youngerhousea.miraidesktop.utils.withoutWidthConstraints

@Composable
fun BotListView(model: Model) = Surface(
    modifier = Modifier
        .fillMaxSize()
        .padding(30.dp)
) {
    Box(Modifier.fillMaxSize()) {
        val scrollState = rememberLazyListState()
        val itemHeight = 100.dp

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .withoutWidthConstraints(),
            state = scrollState
        ) {
            itemsIndexed(model.bots) { index, item ->
                BotItem(
                    Modifier
                        .preferredHeight(100.dp),
                    item,
                    updateAction = {
                        model.currentIndex = index
                    },
                    removeAction = {
                        model.bots.removeAt(model.currentIndex)
                    },
                )
            }

            item {
                BotAddButton(
                    {
                        model.bots.add(ComposeBot())
                    },
                    Modifier
                        .preferredHeight(100.dp)
                ) {
                    Text("添加一个机器人", fontWeight = FontWeight.Bold)
                }
            }
        }

        VerticalScrollbar(
            Modifier.align(Alignment.CenterEnd),
            scrollState,
            model.bots.size + 1,
            itemHeight
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalScrollbar(
    modifier: Modifier,
    scrollState: LazyListState,
    itemCount: Int,
    averageItemSize: Dp
) = VerticalScrollbar(
    rememberScrollbarAdapter(scrollState, itemCount, averageItemSize),
    modifier
)

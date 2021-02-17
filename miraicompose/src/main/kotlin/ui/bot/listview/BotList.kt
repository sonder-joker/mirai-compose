package com.youngerhousea.miraicompose.ui.bot.listview

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.model.Model
import com.youngerhousea.miraicompose.utils.withoutWidthConstraints

@Composable
fun TopView(modifier: Modifier) = Surface {
    Row(modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Bots",
            color = LocalContentColor.current.copy(alpha = 0.60f),
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun BotListView(model: Model, modifier: Modifier = Modifier) = Surface(modifier
) {
    Box(Modifier.fillMaxSize()) {
        val scrollState = rememberLazyListState()
        val itemHeight = 100.dp

        LazyColumn(
            Modifier
                .fillMaxSize()
                .withoutWidthConstraints(),
            state = scrollState
        ) {
            itemsIndexed(model.bots) { index, item ->
                BotItem(
                    item,
                    Modifier
                        .requiredHeight(itemHeight),
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
                        .requiredHeight(itemHeight)
                ) {
                    Text("添加一个Bot", fontWeight = FontWeight.Bold)
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

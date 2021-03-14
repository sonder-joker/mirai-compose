package com.youngerhousea.miraicompose.ui.feature.bot.listview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.utils.VerticalScrollbar
import com.youngerhousea.miraicompose.utils.withoutWidthConstraints

@Composable
fun TopView(modifier: Modifier) = Surface {
    Row(
        modifier,
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
fun BotListView(
    model: MutableList<ComposeBot>,
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit,
    onItemClick: (bot: ComposeBot) -> Unit,
    onItemRemove: (bot: ComposeBot) -> Unit
) = Box(modifier) {
    val scrollState = rememberLazyListState()
    val itemHeight = 100.dp

    LazyColumn(
        Modifier
            .fillMaxSize()
            .withoutWidthConstraints(),
        state = scrollState
    ) {
        items(model) { item ->
            BotItem(
                item,
                Modifier
                    .requiredHeight(itemHeight),
                onItemClick = {
                    onItemClick(item)
                },
                onItemRemove = {
                    onItemRemove(item)
                }
            )
        }

        item {
            Button(
                onClick = onButtonClick,
                modifier = Modifier
                    .requiredHeight(itemHeight)
                    .aspectRatio(2f)
                    .padding(24.dp),
                colors = ButtonDefaults.buttonColors(
                ),
                content = {
                    Text("添加一个Bot", fontWeight = FontWeight.Bold)
                }
            )
        }
    }

    VerticalScrollbar(
        Modifier.align(Alignment.CenterEnd),
        scrollState,
        model.size + 1,
        itemHeight
    )
}




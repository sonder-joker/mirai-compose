package com.youngerhousea.miraicompose.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.ui.feature.bot.BotItem

@Composable
internal fun AvatarColumn(
    composeBotList: MutableList<ComposeBot>,
    currentBot: ComposeBot?,
    onItemSelected: (ComposeBot) -> Unit
) {
    var isExpand by remember { mutableStateOf(false) }

    Box {
        currentBot?.let { bot ->
            Row(
                modifier = Modifier
                    .combinedClickable(
                        onLongClick = { isExpand = !isExpand },
                        onClick = { onItemSelected(bot) })
                    .fillMaxWidth()
                    .requiredHeight(80.dp)
            ) {
                BotItem(bot)
            }
        } ?: Row(
            modifier = Modifier
                .combinedClickable(
                    onLongClick = { isExpand = !isExpand },
                    onClick = { })
                .fillMaxWidth()
                .requiredHeight(80.dp)
        ) {
            Text("No item")
        }

        DropdownMenu(isExpand, onDismissRequest = {}) {
            DropdownMenuItem(onClick = { isExpand = !isExpand }) {
                Text("exit")
            }
            DropdownMenuItem(onClick = {
                isExpand = !isExpand
                val new = ComposeBot()
                composeBotList.add(new)
                onItemSelected(new)
            }) {
                Text("Add")
            }
            for (item in composeBotList) {
                DropdownMenuItem(onClick = {
                    onItemSelected(item)
                }) {
                    BotItem(item)
                }
            }
        }
    }
}

@Composable
internal fun SelectEdgeText(text: String, isWishWindow: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .requiredHeight(80.dp)
            .background(if (isWishWindow) MaterialTheme.colors.background else MaterialTheme.colors.primary),
        contentAlignment = Alignment.Center
    ) {
        if (isWishWindow)
            Text(text, style = MaterialTheme.typography.subtitle1)
        else
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(text, style = MaterialTheme.typography.subtitle1)
            }
    }
}
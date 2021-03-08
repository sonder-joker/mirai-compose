package com.youngerhousea.miraicompose.ui.feature.bot.listview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.model.ComposeBot

@Composable
fun BotItem(
    bot: ComposeBot,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
    onItemRemove: () -> Unit,
) {
    Row(
        modifier = modifier
            .aspectRatio(2f)
            .clickable(onClick = onItemClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .weight(2f, fill = false)
                .requiredSize(60.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ) {
            Image(bot.avatar, null)
        }

        Column(
            Modifier
                .weight(3f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(bot.nick, fontWeight = FontWeight.Bold)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(bot.id.toString(), style = MaterialTheme.typography.body2)
            }
        }

        Column(
            Modifier
                .weight(1f)
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete",
                modifier = Modifier
                    .clickable(onClick = onItemRemove)
            )
        }
    }
}

@Composable
fun BotAddButton(
    onClick: () -> Unit,
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(2f)
            .padding(24.dp),
        backgroundColor = MaterialTheme.colors.background,
        content = content
    )
}
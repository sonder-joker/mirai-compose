package com.youngerhousea.miraicompose.ui.botlistwindows

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.theme.AppTheme

@Composable
fun BotItem(
    modifier: Modifier = Modifier,
    bot: ComposeBot,
    updateAction: () -> Unit,
    removeAction: () -> Unit,
) {
    Row(
        modifier = modifier
            .aspectRatio(2f)
            .clickable(onClick = updateAction),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .weight(2f, fill = false)
                .preferredSize(60.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ) {
            Image(bot.avatar, "Í·Ïñ")
        }

        Column(
            Modifier
                .weight(3f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(bot.nick, fontWeight = FontWeight.Bold)
            Providers(LocalContentAlpha provides ContentAlpha.medium) {
                Text(bot.id, style = MaterialTheme.typography.body2)
            }
        }
//        Icon(
//            Icons.Default.Delete,
//            contentDescription = "É¾³ý",
//            modifier = Modifier
//                .weight(1f)
//                .clickable(onClick = removeAction)
//        )
        //TODO:https://github.com/JetBrains/compose-jb/issues/338

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
        backgroundColor = AppTheme.colors.backgroundDark,
        content = content
    )
}
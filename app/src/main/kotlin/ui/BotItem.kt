package com.youngerhousea.mirai.compose.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.font.FontWeight
import com.youngerhousea.mirai.compose.ui.login.AsyncImage
import io.ktor.client.request.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.Mirai

@Composable
fun BotItem(
    bot: Bot,
) {
    BotContent(
        avatar = {
            AsyncImage(load = {
                loadImageBitmap(Mirai.Http.get(bot.avatarUrl))
            }, painterFor = {
                BitmapPainter(it)
            }, null)
        },
        nick = bot.run {
            try {
                nick
            } catch (e: UninitializedPropertyAccessException) {
                "Unknown"
            }
        },
        id = bot.id.toString()
    )
}

@Composable
fun EmptyBotItem() {
    BotContent(
        avatar = {
            ImageBitmap(200, 200)
        },
        nick = "NoLogin",
        id = "Unknown"
    )
}


@Composable
fun BotContent(
    modifier: Modifier = Modifier,
    avatar: @Composable () -> Unit,
    nick: String,
    id: String
) {
    Row(
        modifier = modifier
            .aspectRatio(2f)
            .clipToBounds(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.weight(1f).fillMaxHeight())
        Surface(
            modifier = Modifier
                .weight(3f, fill = false),
            shape = CircleShape,
            color = Color(0xff979595),
        ) {
            avatar()
        }
        Column(
            Modifier
                .weight(6f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(nick, fontWeight = FontWeight.Bold, maxLines = 1)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(id, style = MaterialTheme.typography.body2)
            }
        }
    }
}

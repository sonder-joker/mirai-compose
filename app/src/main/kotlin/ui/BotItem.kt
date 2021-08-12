package com.youngerhousea.mirai.compose.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.font.FontWeight
import com.youngerhousea.mirai.compose.console.viewModel
import com.youngerhousea.mirai.compose.viewmodel.BotViewModel
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.MiraiLogger
import kotlin.coroutines.CoroutineContext

object EmptyBot : Bot {
    override val asFriend: Friend
        get() = error("Not yet implemented")
    override val asStranger: Stranger
        get() = error("Not yet implemented")
    override val configuration: BotConfiguration
        get() = error("Not yet implemented")
    override val coroutineContext: CoroutineContext
        get() = error("Not yet implemented")
    override val eventChannel: EventChannel<BotEvent>
        get() = error("Not yet implemented")
    override val friends: ContactList<Friend>
        get() = error("Not yet implemented")
    override val groups: ContactList<Group>
        get() = error("Not yet implemented")
    override val id: Long = 123456789
    override val isOnline: Boolean
        get() = error("Not yet implemented")
    override val logger: MiraiLogger
        get() = error("Not yet implemented")
    override val nick: String = "No Login"

    override val otherClients: ContactList<OtherClient>
        get() = error("Not yet implemented")
    override val strangers: ContactList<Stranger>
        get() = error("Not yet implemented")

    override fun close(cause: Throwable?) {
        error("Not yet implemented")
    }

    override suspend fun login() {
        error("Not yet implemented")
    }

}

@Composable
fun EmptyBotItem() {
    BotContent(
        avatar = ImageBitmap(200, 200),
        nick = "NoLogin",
        id = "Unknown"
    )
}

@Composable
fun BotItem(
    bot: Bot,
    botViewModel: BotViewModel = viewModel(bot) { BotViewModel(bot) }
) {
    BotContent(
        avatar = botViewModel.avatar.value,
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
fun BotContent(
    modifier: Modifier = Modifier,
    avatar: ImageBitmap,
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
            Image(avatar, null)
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

@Preview
@Composable
fun BotContentPreview() {
    BotContent(
        avatar = ImageBitmap(200, 200),
        nick = "test",
        id = "123456789"
    )
}

@Preview
@Composable
fun EmptyBotItemPreview() {
    EmptyBotItem()
}

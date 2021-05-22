package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.ui.graphics.ImageBitmap
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.utils.fakeContext
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.MiraiLogger
import kotlin.coroutines.CoroutineContext

class FakeComposeBot : ComposeBot {
    override val avatar: ImageBitmap
        get() = TODO("Not yet implemented")
    override val messageSpeed: Int
        get() = TODO("Not yet implemented")
    override val asFriend: Friend
        get() = TODO("Not yet implemented")
    override val asStranger: Stranger
        get() = TODO("Not yet implemented")
    override val configuration: BotConfiguration
        get() = TODO("Not yet implemented")
    override val coroutineContext: CoroutineContext
        get() = TODO("Not yet implemented")
    override val eventChannel: EventChannel<BotEvent>
        get() = TODO("Not yet implemented")
    override val friends: ContactList<Friend>
        get() = TODO("Not yet implemented")
    override val groups: ContactList<Group>
        get() = TODO("Not yet implemented")
    override val id: Long
        get() = TODO("Not yet implemented")
    override val isOnline: Boolean
        get() = TODO("Not yet implemented")
    override val logger: MiraiLogger
        get() = TODO("Not yet implemented")
    override val nick: String
        get() = TODO("Not yet implemented")
    override val otherClients: ContactList<OtherClient>
        get() = TODO("Not yet implemented")
    override val strangers: ContactList<Stranger>
        get() = TODO("Not yet implemented")

    override fun close(cause: Throwable?) {
        TODO("Not yet implemented")
    }

    override suspend fun login() {
        TODO("Not yet implemented")
    }
}

class OnlineBotTest {
//    val onlineBot = Message(fakeContext(), FakeComposeBot())

}
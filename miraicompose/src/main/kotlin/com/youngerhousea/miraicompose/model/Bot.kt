package com.youngerhousea.miraicompose.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.ImageBitmap
import com.youngerhousea.miraicompose.utils.toAvatarImage
import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.MiraiLogger
import kotlin.coroutines.CoroutineContext

interface ComposBot {

    val logger: MiraiLogger

    val messages: SnapshotStateList<MessageEvent>

    val event: SnapshotStateList<BotEvent>

    val nick: String

    val id: String

    val avatar: ImageBitmap

    suspend fun login(account: String, password: String)
}

class ComposBotImpl : ComposBot {
    private var _bot: Bot? = null

    private var _avatar by mutableStateOf(ImageBitmap(200, 200))

    var state: BotState by mutableStateOf(BotState.None)

    override val logger: MiraiLogger get() = _bot?.logger!!


    override val messages = mutableStateListOf<MessageEvent>()

    override val event = mutableStateListOf<BotEvent>()

    override val nick
        get() = when (state) {
            BotState.None -> "Î´µÇÂ¼"
            BotState.Loading -> "¼ÓÔØÖÐ"
            BotState.Login -> _bot!!.nick
        }

    override val id
        get() = when (state) {
            BotState.None, BotState.Loading -> "..."
            BotState.Login -> _bot!!.id.toString()
        }


    override val avatar
        get() = when (state) {
            BotState.None, BotState.Loading -> _avatar
            BotState.Login -> {
                _avatar
            }
        }


    override suspend fun login(account: String, password: String) {
        state = BotState.Loading
        _bot = BotFactory.newBot(account.toLong(), password, BotConfiguration().apply {
            fileBasedDeviceInfo()
        }).alsoLogin()
        state = BotState.Login
        this._avatar = _bot!!.avatarUrl.toAvatarImage()
        _bot!!.eventChannel.subscribeAlways<MessageEvent> {
            messages.add(this)
        }
        _bot!!.eventChannel.subscribeAlways<BotEvent> {
            event.add(this)
        }
    }

}


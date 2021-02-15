package com.youngerhousea.miraicompose.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.AnnotatedString
import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.utils.toAvatarImage
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.utils.MiraiLogger

interface ComposeBot {
    var state: BotState

    val logger: MiraiLogger

    val event: SnapshotStateList<BotEvent>

    val nick: String

    val id: String

    val avatar: ImageBitmap

    val log: SnapshotStateList<AnnotatedString>

    suspend fun login(account: String, password: String)

    companion object {
        operator fun invoke(bot: Bot? = null): ComposeBot = ComposeBotImpl(bot)
    }
}

internal class ComposeBotImpl(bot: Bot? = null) : ComposeBot {
    private var _bot: Bot? = bot

    private var _avatar by mutableStateOf(ImageBitmap(200, 200))

    override var state: BotState by mutableStateOf(BotState.None)

    override val logger: MiraiLogger get() = _bot!!.logger

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
        get() = _avatar

    override val log: SnapshotStateList<AnnotatedString> = mutableStateListOf()


    override suspend fun login(account: String, password: String) {
        if (state == BotState.Login)
            _bot!!.login()
        else {
            state = BotState.Loading
            _bot = MiraiConsole.addBot(account.toLong(), password) {
                fileBasedDeviceInfo()
            }.alsoLogin()
            state = BotState.Login
            loadResource()
        }
    }

    init {
        if (bot != null) {
            state = BotState.Login
            MiraiCompose.launch {
                loadResource()
            }
        }
    }

    private suspend fun loadResource() {
        _avatar = _bot!!.avatarUrl.toAvatarImage()

        _bot!!.eventChannel.subscribeAlways<BotEvent> {
            event.add(this)
        }
    }

}

enum class BotState {
    None, Loading, Login
}
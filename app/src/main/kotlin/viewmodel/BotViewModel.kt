package com.youngerhousea.mirai.compose.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import com.youngerhousea.mirai.compose.console.MiraiCompose
import com.youngerhousea.mirai.compose.console.ViewModelScope
import com.youngerhousea.mirai.compose.console.impl.doOnFinishLoading
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.Mirai

class BotViewModel(bot: Bot) : ViewModelScope() {

    private val _image = mutableStateOf(ImageBitmap(200, 200))

    val avatar: State<ImageBitmap> get() = _image

    init {
        MiraiCompose.lifecycle.doOnFinishLoading {
             viewModelScope.launch(Dispatchers.IO) {
                _image.value = loadImageBitmap(Mirai.Http.get(bot.avatarUrl))
            }
        }
    }

}


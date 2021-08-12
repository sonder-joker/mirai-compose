package com.youngerhousea.mirai.compose.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.youngerhousea.mirai.compose.console.MiraiCompose
import com.youngerhousea.mirai.compose.console.ViewModelScope
import com.youngerhousea.mirai.compose.console.doOnFinishLoading
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.Mirai
import org.jetbrains.skija.Image

class BotViewModel(bot: Bot) : ViewModelScope() {

    private val _image = mutableStateOf(ImageBitmap(200, 200))

    val avatar: State<ImageBitmap> get() = _image

    init {
        MiraiCompose.lifecycle.doOnFinishLoading {
             viewModelScope.launch(Dispatchers.IO) {
                _image.value = skiaImageDecode(Mirai.Http.get(bot.avatarUrl))
            }
        }
    }

}


internal fun skiaImageDecode(byteArray: ByteArray): ImageBitmap =
    Image.makeFromEncoded(byteArray).asImageBitmap()
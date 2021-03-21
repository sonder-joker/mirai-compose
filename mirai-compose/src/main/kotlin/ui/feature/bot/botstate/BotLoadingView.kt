package com.youngerhousea.miraicompose.ui.feature.bot.botstate

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.SkiaImageDecode
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot

class BotLo(context: ComponentContext, val bot: Bot, val data: ByteArray, val result: suspend (String?) -> Unit) :
    Component, ComponentContext by context {
    @Composable
    override fun render() {
        BotLoView(bot, data, result)
    }
}

@Composable
fun BotLoView(bot: Bot, data: ByteArray, result: suspend (String?) -> Unit) {
    val image = SkiaImageDecode(data)
    val scope = rememberCoroutineScope()
    var value by mutableStateOf(TextFieldValue())
    Column {
        Text("Mirai PicCaptcha(${bot.id})")
        Image(image, null)
        TextField(value = value, onValueChange = { value = it })
        Button(onClick = { scope.launch { result(value.text) } }) {
            Text("Sure")
        }
    }
}

class BotLoad(context: ComponentContext, val bot: Bot, val url: String, result: suspend (String?) -> Unit) : Component, ComponentContext by context {
    @Composable
    override fun render() {
        BotLoadView(bot, url)
    }
}

@Composable
fun BotLoadView(bot: Bot, url: String) {
    TODO("嵌入一下.jpg")
}

class BotLoading(context: ComponentContext, val bot: Bot, val url: String, result: suspend (String?) -> Unit) : Component, ComponentContext by context {
    @Composable
    override fun render() {
        BotLoadingView(bot, url)
    }
}

@Composable
fun BotLoadingView(bot: Bot, url: String) = Box(
    Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    TODO("嵌入一下.jpg")
}

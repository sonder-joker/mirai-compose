package com.youngerhousea.miraicompose.ui.feature.bot.state

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
import com.youngerhousea.miraicompose.utils.SkiaImageDecode
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot

class BotSolvePicCaptchaLoading(
    context: ComponentContext,
    val bot: Bot,
    val data: ByteArray,
    val result: suspend (String?) -> Unit
) : ComponentContext by context {

}

@Composable
fun BotSolvePicCaptchaLoadingUi(botSolvePicCaptchaLoading: BotSolvePicCaptchaLoading) {
    val image = SkiaImageDecode(botSolvePicCaptchaLoading.data)
    val scope = rememberCoroutineScope()
    var value by mutableStateOf(TextFieldValue())
    Column {
        Text("Mirai PicCaptcha(${botSolvePicCaptchaLoading.bot.id})")
        Image(image, null)
        TextField(value = value, onValueChange = { value = it })
        Button(onClick = { scope.launch { botSolvePicCaptchaLoading.result(value.text) } }) {
            Text("Sure")
        }
    }
}

class BotSolveSliderCaptchaLoading(
    context: ComponentContext,
    val bot: Bot,
    val url: String,
    val result: suspend (String?) -> Unit
) : ComponentContext by context {

}

@Composable
fun BotSolveSliderCaptchaLoadingUi(botSolveSliderCaptchaLoading: BotSolveSliderCaptchaLoading) {
    TODO("嵌入一下.jpg")
}

class BotSolveUnsafeDeviceLoginVerify(
    context: ComponentContext,
    val bot: Bot,
    val url: String,
    val result: suspend (String?) -> Unit
) : ComponentContext by context {

}

@Composable
fun BotSolveUnsafeDeviceLoginVerifyUi(botSolveUnsafeDeviceLoginVerify: BotSolveUnsafeDeviceLoginVerify) = Box(
    Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    TODO("嵌入一下.jpg")
}

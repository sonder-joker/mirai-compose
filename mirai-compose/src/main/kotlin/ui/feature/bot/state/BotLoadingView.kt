package com.youngerhousea.miraicompose.ui.feature.bot.state

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.utils.SkiaImageDecode
import com.youngerhousea.miraicompose.utils.splitQuery
import net.mamoe.mirai.Bot
import net.mamoe.mirai.network.CustomLoginFailedException
import java.net.URL

class BotSolvePicCaptcha(
    context: ComponentContext,
    val bot: Bot,
    val data: ByteArray,
    val result: (String?) -> Unit
) : ComponentContext by context

@Composable
fun BotSolvePicCaptchaUi(botSolvePicCaptcha: BotSolvePicCaptcha) {
    val image = SkiaImageDecode(botSolvePicCaptcha.data)
    var value by mutableStateOf(TextFieldValue())
    Column {
        Text("Mirai PicCaptcha(${botSolvePicCaptcha.bot.id})")
        Image(image, null)
        TextField(value = value, onValueChange = { value = it })
        Button(onClick = { botSolvePicCaptcha.result(value.text) }) {
            Text("Sure")
        }
    }
}

class BotSolveSliderCaptcha(
    context: ComponentContext,
    val bot: Bot,
    val url: String,
    val result: (String?) -> Unit
) : ComponentContext by context

@Composable
fun BotSolveSliderCaptchaUi(botSolveSliderCaptcha: BotSolveSliderCaptcha) {
    TODO("嵌入一下.jpg")
}

class BotSolveUnsafeDeviceLoginVerify(
    context: ComponentContext,
    val bot: Bot,
    url: String,
    val result: (String?) -> Unit
) : ComponentContext by context {
    private val qrCodeParameter = URL(url).splitQuery()

    private val sig get() = qrCodeParameter["sig"] ?: error("Error to get sig")

    val qrCodeUrl get() = "https://ti.qq.com/safe/qrcode?uin=${bot.id}&sig=${sig}"
}

@Composable
fun BotSolveUnsafeDeviceLoginVerifyUi(botSolveUnsafeDeviceLoginVerify: BotSolveUnsafeDeviceLoginVerify) = Box(
    Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    Column {
        Text("Mirai UnsafeDeviceLoginVerify(${botSolveUnsafeDeviceLoginVerify.bot.id}")
        SelectionContainer {
            Text(botSolveUnsafeDeviceLoginVerify.qrCodeUrl)
        }
        Row {
            Button(onClick = {
                botSolveUnsafeDeviceLoginVerify.result(null)
            }) {
                Text("Sure")
            }
            Button(onClick = {
                throw ExitException()
            }) {
                Text("Return")
            }
        }
    }
}

class ExitException : CustomLoginFailedException(true)

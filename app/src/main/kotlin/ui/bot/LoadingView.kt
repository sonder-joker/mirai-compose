package com.youngerhousea.miraicompose.ui.bot

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
import com.youngerhousea.miraicompose.component.bot.SolvePicCaptcha
import com.youngerhousea.miraicompose.component.bot.SolveSliderCaptcha
import com.youngerhousea.miraicompose.component.bot.SolveUnsafeDeviceLoginVerify
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.mamoe.mirai.network.CustomLoginFailedException


@Composable
fun SolvePicCaptchaUi(solvePicCaptcha: SolvePicCaptcha) {
    var value by mutableStateOf(TextFieldValue())
    Column {
        Text("Mirai PicCaptcha(${solvePicCaptcha.bot.id})")
        Image(solvePicCaptcha.imageBitmap, null)
        TextField(value = value, onValueChange = { value = it })
        Row {
            Button(onClick = { solvePicCaptcha.result(null, ReturnException()) }) {
                Text("Exit")
            }
            Button(onClick = { solvePicCaptcha.result(value.text, null) }) {
                Text("Sure")
            }
        }
    }
}

@Composable
fun SolveSliderCaptchaUi(solveSliderCaptcha: SolveSliderCaptcha) {
    Column {
        Text("嵌入一下.jpg")
        Text(solveSliderCaptcha.url)
    }
}


@Composable
fun SolveUnsafeDeviceLoginVerifyUi(solveUnsafeDeviceLoginVerify: SolveUnsafeDeviceLoginVerify) = Box(
    Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    Column {
        Text("Mirai UnsafeDeviceLoginVerify(${solveUnsafeDeviceLoginVerify.bot.id}")

        SelectionContainer {
            Text(solveUnsafeDeviceLoginVerify.qrCodeUrl)
        }
        Row {
            Button(onClick = {
                solveUnsafeDeviceLoginVerify.result(null, null)
            }) {
                Text("Sure")
            }
            Button(onClick = {
                solveUnsafeDeviceLoginVerify.result(null, ReturnException())
            }) {
                Text("Return")
            }
        }
    }
}

@Serializable
data class Req1(
    @SerialName("str_dev_auth_token") val s: String,
    @SerialName("uint32_flag") val flag: Int
)

@Serializable
data class Req(
    @SerialName("bytes_token") val bytesToken: String,
    @SerialName("uint32_flag") val flag: Int
)

@Serializable
data class Res(
    @SerialName("str_url") val strUrl: String = "",
    @SerialName("ActionStatus") val actionStatus: String,
    @SerialName("ErrorCode") val errorCode: Int,
    @SerialName("ErrorInfo") val errorInfo: String,
    @SerialName("WaterKeyInfo") val waterKeyInfo: String
)

class ReturnException(killBot: Boolean = true, message: String = "返回") : CustomLoginFailedException(killBot, message)


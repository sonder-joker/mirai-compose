package com.youngerhousea.miraicompose.ui.feature.bot

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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.utils.ComponentScope
import com.youngerhousea.miraicompose.utils.splitQuery
import io.ktor.client.*
import io.ktor.client.features.cookies.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.Bot
import net.mamoe.mirai.network.CustomLoginFailedException
import java.net.URL

class SolvePicCaptcha(
    context: ComponentContext,
    val bot: Bot,
    val imageBitmap: ImageBitmap,
    val result: (String?, ReturnException?) -> Unit
) : ComponentContext by context

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

class SolveSliderCaptcha(
    context: ComponentContext,
    val bot: Bot,
    val url: String,
    val result: (String?, ReturnException?) -> Unit
) : ComponentContext by context

@Composable
fun SolveSliderCaptchaUi(solveSliderCaptcha: SolveSliderCaptcha) {
    Column {
        Text("嵌入一下.jpg")
        Text(solveSliderCaptcha.url)
    }
}

class SolveUnsafeDeviceLoginVerify(
    context: ComponentContext,
    val bot: Bot,
    url: String,
    val result: (String?, ReturnException?) -> Unit,
) : ComponentContext by context {

    private val client = HttpClient() {
        install(HttpCookies)
    }

    //need cookie
    private suspend fun firstRequest(sig: String): String {
        val s =
            client.post<String>("https://ti.qq.com/proxy/domain/oidb.tim.qq.com/v3/oidbinterface/oidb_0xc9e_4?sdkappid=39998&actype=2") {
                accept(ContentType.Application.Json)
                body = "{\"str_dev_auth_token\":\"${sig}\",\"uint32_flag\":1}"
            }
        val res = Json { ignoreUnknownKeys = true }.decodeFromString<Res>(s)
//        if (res.errorCode != 0)
//            error(res.errorInfo)
        return res.strUrl
    }

    private val scope = ComponentScope()

    private val qrCodeParameter = URL(url).splitQuery()

    private val sig get() = qrCodeParameter["sig"] ?: error("Error to get sig")

    val qrCodeUrl get() = "https://ti.qq.com/safe/qrcode?uin=${bot.id}&sig=${sig}"

    var string by mutableStateOf("")

    init {
        println(url)
//        val value = scope.async { firstRequest(sig) }
//        scope.launch {
//            string = value.await()
//            while (true) {
//                val s =
//                    client.post<String>("https://ti.qq.com/proxy/domain/oidb.tim.qq.com/v3/oidbinterface/oidb_0xc9e_4?sdkappid=39998&actype=2&bkn=") {
//                        accept(ContentType.Application.Json)
//                        body = "{\"str_dev_auth_token\":\"${string}\",\"uint32_flag\":0}"
//                    }
//                val res = Json { ignoreUnknownKeys = true }.decodeFromString<Res>(s)
//                string = res.strUrl
//                delay(1_000)
//            }
//        }

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


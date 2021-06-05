package com.youngerhousea.miraicompose.component.impl.bot

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.component.bot.SolveUnsafeDeviceLoginVerify
import com.youngerhousea.miraicompose.ui.bot.Res
import com.youngerhousea.miraicompose.ui.bot.ReturnException
import com.youngerhousea.miraicompose.utils.ComponentScope
import com.youngerhousea.miraicompose.utils.splitQuery
import io.ktor.client.*
import io.ktor.client.features.cookies.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.Bot
import java.net.URL

class SolveUnsafeDeviceLoginVerifyImpl(
    context: ComponentContext,
    override val bot: Bot,
    url: String,
    override val result: (String?, ReturnException?) -> Unit,
) : SolveUnsafeDeviceLoginVerify, ComponentContext by context {

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

    override val qrCodeUrl get() = "https://ti.qq.com/safe/qrcode?uin=${bot.id}&sig=${sig}"

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
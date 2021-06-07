package com.youngerhousea.miraicompose.core.component.impl.bot

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.core.component.bot.ReturnException
import com.youngerhousea.miraicompose.core.component.bot.SolveUnsafeDeviceLoginVerify
import com.youngerhousea.miraicompose.core.utils.componentScope
import com.youngerhousea.miraicompose.core.utils.splitQuery
import io.ktor.client.*
import io.ktor.client.features.cookies.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.Bot
import java.net.URL

internal class SolveUnsafeDeviceLoginVerifyImpl(
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

    private val scope = componentScope()

    private val qrCodeParameter = URL(url).splitQuery()

    private val sig get() = qrCodeParameter["sig"] ?: error("Error to get sig")

    override val qrCodeUrl get() = "https://ti.qq.com/safe/qrcode?uin=${bot.id}&sig=${sig}"


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


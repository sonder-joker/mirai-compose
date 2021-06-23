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
    override val qrCodeUrl: String,
    inline val onFinish: (String?, ReturnException?) -> Unit,
) : SolveUnsafeDeviceLoginVerify, ComponentContext by context {
    override fun onExcept() {
        onFinish(null, ReturnException())
    }

    override fun onSuccess() {
        onFinish(null, null)
    }
}


package com.youngerhousea.miraicompose.web.internal

import io.vertx.core.Vertx
import kotlinx.html.CommonAttributeGroupFacade

interface ServerScope {
    fun CommonAttributeGroupFacade.onClick(action: () -> Unit): String
}

internal class ServerScopeImpl : ServerScope {
    val vertx = Vertx.vertx()

    val server = vertx.createHttpServer()

    val eventBus = vertx.eventBus()

    val list = mutableListOf<String>()

    override fun CommonAttributeGroupFacade.onClick(action: () -> Unit): String {
        server.webSocketHandler {

        }

        return """
            ${action.hashCode()}
        """.trimIndent()
    }

    init {
        server.requestHandler {

        }
    }


}
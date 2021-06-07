package com.youngerhousea.miraicompose.web

import io.vertx.core.Vertx

object MiraiComposeLoader {
    @JvmStatic
    fun main(args: Array<String>) {
        val vertx = Vertx.vertx()
        val ser = vertx.createHttpServer()

        ser.requestHandler {
            it.response().end("Hello world")
        }.listen(9001, "127.0.0.1")
    }
}


package com.youngerhousea.miraicompose.web

import io.vertx.core.Promise.promise
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import kotlinx.html.*
import kotlinx.html.dom.createHTMLDocument
import org.w3c.dom.Document
import java.io.StringWriter
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


fun CommonAttributeGroupFacade.onClickFunction(action: () -> Unit) {

}

object MiraiComposeLoader {
    val a = createHTMLDocument().html {
        head {
            script {
                unsafe {
                    +("")
                }
            }
        }
        body {
            div {
                button {
                    onClick = """
                        const onClick = async () => {
                            let response = await fetch(`http://127.0.0.1:9001/home/${onClick.hashCode()}`)
                        }
                    """.trimIndent()
                    +"Test"
                }
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val vertx = Vertx.vertx()
        val ser = vertx.createHttpServer()

        ser.webSocketHandler { webSocket ->
            webSocket.writeTextMessage("haha")
        }

        ser.listen(9001, "127.0.0.1")
    }
}

val Document.content: String
    get() {
        val writer = StringWriter()
        val result = StreamResult(writer)
        val tf = TransformerFactory.newInstance()
        val transformer = tf.newTransformer()
        transformer.transform(DOMSource(this), result)
        return writer.toString()
    }
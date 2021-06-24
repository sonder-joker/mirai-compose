package com.youngerhousea.miraicompose.web

import com.vaadin.flow.component.button.Button
import com.youngerhousea.miraicompose.core.component.NavHost
import com.youngerhousea.miraicompose.core.navHost


object MiraiComposeLoader {

    @JvmStatic
    fun main(args: Array<String>) {
        val navHost = navHost()
        NavHost(navHost)
    }
}

fun NavHost(navHost: NavHost) {
    val button = Button("") {
        
    }
}
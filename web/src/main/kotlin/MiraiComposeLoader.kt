package com.youngerhousea.miraicompose.web

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
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
    val verticalLayout = VerticalLayout()
    val a = FlexLayout()
    val button = Button("") {

    }
    a.add()

}

operator fun HasComponents.plus(components: Component) {
    this.add(components)
}

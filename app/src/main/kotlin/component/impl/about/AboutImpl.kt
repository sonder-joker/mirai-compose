package com.youngerhousea.miraicompose.component.impl.about

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.component.about.About
import com.youngerhousea.miraicompose.console.MiraiCompose
import net.mamoe.mirai.console.MiraiConsole

class AboutImpl(
    componentContext: ComponentContext
) : About, ComponentContext by componentContext {

    override val frontend = "Frontend V ${MiraiCompose.frontEndDescription.version}"

    override val backend = "Backend V ${MiraiConsole.version}"

    val miraiForum = buildAnnotatedString {
        pushStringAnnotation(
            tag = "URL",
            annotation = "https://mirai.mamoe.net/",
        )
        withStyle(
            style = SpanStyle(
                color = Color.Blue,
                fontWeight = FontWeight.Bold
            )
        ) {
            append("Mirai Forum")
        }
        pop()
    }
}
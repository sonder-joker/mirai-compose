package com.youngerhousea.miraicompose.ui.feature.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.console.MiraiCompose
import com.youngerhousea.miraicompose.theme.ResourceImage
import com.youngerhousea.miraicompose.utils.Desktop
import net.mamoe.mirai.Mirai
import net.mamoe.mirai.console.MiraiConsole
import java.net.URI

/**
 * 关于MiraiCompose的信息
 *
 */
class About(
    componentContext: ComponentContext
) : ComponentContext by componentContext {

    val frontend = buildAnnotatedString {
        pushStringAnnotation(
            tag = "URL",
            annotation = "https://github.com/sonder-joker/mirai-compose",
        )
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold
            )
        ) {
            append("Frontend V ${MiraiCompose.frontEndDescription.version}")
        }
        pop()

    }
    val backend = buildAnnotatedString {
        pushStringAnnotation(
            tag = "URL",
            annotation = "https://github.com/mamoe/mirai",
        )
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold
            )
        ) {
            append("Backend V ${MiraiConsole.version}")
        }
        pop()
    }
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

@Composable
fun AboutUi(about: About) {
    Row(
        Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(ResourceImage.mirai, "mirai")
        Column {
            ClickableUrlText(about.frontend)
            ClickableUrlText(about.backend)
        }
    }
}

@Composable
fun ClickableUrlText(annotatedString: AnnotatedString) {
    ClickableText(annotatedString) { offset ->
        annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset).firstOrNull()
            ?.let { annotation ->
                Desktop.browse(URI(annotation.item))
            }
    }
}
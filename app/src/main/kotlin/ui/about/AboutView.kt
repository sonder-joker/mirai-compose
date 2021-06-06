package com.youngerhousea.miraicompose.app.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.youngerhousea.miraicompose.core.component.about.About
import com.youngerhousea.miraicompose.app.utils.ResourceImage
import com.youngerhousea.miraicompose.app.utils.Desktop
import net.mamoe.mirai.console.MiraiConsole
import java.net.URI

@Composable
fun AboutUi(about: About) {
    val frontend = remember {
        buildAnnotatedString {
            pushStringAnnotation(
                tag = "URL",
                annotation = "https://github.com/sonder-joker/mirai-compose",
            )
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(about.frontend)
            }
            pop()
        }
    }

    val backend =  buildAnnotatedString {
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

    Row(
        Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(ResourceImage.mirai, "mirai")
        Column {
            ClickableUrlText(frontend)
            ClickableUrlText(backend)
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
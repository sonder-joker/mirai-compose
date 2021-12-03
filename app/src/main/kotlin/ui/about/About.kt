package com.youngerhousea.mirai.compose.ui.about

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.youngerhousea.mirai.compose.resource.R
import net.mamoe.mirai.console.MiraiConsole
import java.awt.Desktop
import java.net.URI

@Composable
fun About(
) {
    Row(
        Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(R.Image.Mirai, "mirai")
        Column {
            ClickableUrlText(frontendAnnotatedString(R.Version.Frontend))
            ClickableUrlText(backendAnnotatedString(R.Version.Backend))
        }
    }
}

@Composable
private fun frontendAnnotatedString(frontend: String) = remember(frontend) {
    buildAnnotatedString {
        pushStringAnnotation(
            tag = "URL",
            annotation = frontend,
        )
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold
            )
        ) {
            append(frontend)
        }
        pop()
    }
}

@Composable
private fun backendAnnotatedString(backEnd: String) = remember(backEnd) {
    buildAnnotatedString {
        pushStringAnnotation(
            tag = "URL",
            annotation = backEnd,
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
}

@Composable
fun ClickableUrlText(annotatedString: AnnotatedString) {
    SelectionContainer {
        ClickableText(annotatedString) { offset ->
            annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset).firstOrNull()
                ?.let { annotation ->
                    Desktop.getDesktop().browse(URI(annotation.item))
                }
        }
    }
}

@Preview
@Composable
fun AboutPreview() {
    About()
}


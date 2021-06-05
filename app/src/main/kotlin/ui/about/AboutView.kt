package com.youngerhousea.miraicompose.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import com.youngerhousea.miraicompose.component.about.About
import com.youngerhousea.miraicompose.theme.ResourceImage
import com.youngerhousea.miraicompose.utils.Desktop
import java.net.URI

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
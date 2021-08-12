package com.youngerhousea.mirai.compose.ui.loginsolver

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException


//TODO: add a loading animation image bitmap
@Composable
fun <T> AsyncImage(
    load: suspend () -> T,
    painterFor: @Composable (T) -> Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val image: T? by produceState<T?>(null) {
        value = withContext(Dispatchers.IO) {
            try {
                load()
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    if (image != null) {
        Image(
            painter = painterFor(image!!),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    }
}

@Composable
fun LoginSolverContent(
    title: String,
    tip: String,
    value: String?,
    onValueChange: (String?) -> Unit,
    load: suspend () -> ImageBitmap
) {
    Scaffold(
        topBar = {
            Text(title)
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(tip)
            AsyncImage(
                load = load,
                painterFor = { remember { BitmapPainter(it) } },
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
            TextField(value = value ?: "",
                onValueChange = { onValueChange(it.ifEmpty { null }) }
            )
            Text("处理完毕请关闭窗口")
        }
    }
}


@Preview
@Composable
fun LoginSolverContentPreview() {
    LoginSolverContent(
        "Bot:1234567890",
        tip = "tip",
        load = { QRCodeImageBitmap("test", qrCodeHeight = 200, qrCodeWidth = 200) },
        value = "test",
        onValueChange = {}
    )
}
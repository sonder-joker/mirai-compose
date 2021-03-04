package com.youngerhousea.miraicompose.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import net.mamoe.mirai.Bot

private val client = HttpClient()

suspend fun Bot.getAvatarImage(): ImageBitmap {
    return org.jetbrains.skija.Image.makeFromEncoded(
        client.get(this@getAvatarImage.avatarUrl) {
            header("Connection", "close")
        }
    ).asImageBitmap()
}


@Composable
fun VerticalScrollbar(
    modifier: Modifier,
    scrollState: LazyListState,
    itemCount: Int,
    averageItemSize: Dp
) = androidx.compose.foundation.VerticalScrollbar(
    rememberScrollbarAdapter(scrollState, itemCount, averageItemSize),
    modifier
)

fun Modifier.withoutWidthConstraints() = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints.copy(maxWidth = Int.MAX_VALUE))
    layout(constraints.maxWidth, placeable.height) {
        placeable.place(0, 0)
    }
}
package com.youngerhousea.mirai.compose.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@Composable
inline fun <reified T : Enum<T>> EnumTabRowWithContent(
    enum: T,
    rowModifier: Modifier = Modifier,
    crossinline onClick: (enumValue: T) -> Unit,
    crossinline tabContent: @Composable ColumnScope.(enumValue: T) -> Unit
) {
    TabRow(enum.ordinal, modifier = rowModifier) {
        for (current in enumValues<T>()) {
            Tab(enum == current, onClick = {
                onClick(current)
            }, content = {
                tabContent(current)
            })
        }
    }
}

//from Icons.kt and default size is 24
fun closeIcon(size: Float, viewport: Float = size) =
    ImageVector.Builder(
        name = "",
        defaultWidth = size.dp,
        defaultHeight = size.dp,
        viewportWidth = viewport,
        viewportHeight = viewport
    ).materialPath {
        moveToRelative(19.0f, 6.41f)
        lineTo(17.59f, 5.0f)
        lineTo(12.0f, 10.59f)
        lineTo(6.41f, 5.0f)
        lineTo(5.0f, 6.41f)
        lineTo(10.59f, 12.0f)
        lineTo(5.0f, 17.59f)
        lineTo(6.41f, 19.0f)
        lineTo(12.0f, 13.41f)
        lineTo(17.59f, 19.0f)
        lineTo(19.0f, 17.59f)
        lineTo(13.41f, 12.0f)
        close()
    }.build()

fun minIcon(size: Float, viewport: Float = size) =
    ImageVector.Builder(
        name = "",
        defaultWidth = size.dp,
        defaultHeight = size.dp,
        viewportWidth = viewport,
        viewportHeight = viewport
    ).materialPath {
        moveTo(2f, 10.5f)
        horizontalLineTo(22f)
        verticalLineTo(14f)
        horizontalLineTo(2f)
        verticalLineTo(10.5f)
        close()
    }.build()

fun maxIcon(size: Float, viewport: Float = size) =
    ImageVector.Builder(
        name = "",
        defaultWidth = size.dp,
        defaultHeight = size.dp,
        viewportWidth = viewport,
        viewportHeight = viewport
    ).materialPath {
        moveTo(2f, 2f)
        lineTo(20f, 2f)
        lineTo(20f, 5f)
        lineTo(2f, 5f)
        lineTo(2f, 2f)

        moveTo(20f, 2f)
        lineTo(20f, 20f)
        lineTo(17f, 20f)
        lineTo(17f, 2f)

        moveTo(19f, 20f)
        lineTo(2f, 20f)
        lineTo(2f, 17f)
        lineTo(19f, 17f)

        moveTo(2f, 20f)
        lineTo(2f, 2f)
        lineTo(5f, 2f)
        lineTo(5f, 20f)
    }.build()
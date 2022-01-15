package com.youngerhousea.mirai.compose.ui

import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

fun closeIcon(size: Float = 24f, viewport: Float = 24f) =
    ImageVector.Builder(
        name = "",
        defaultWidth = size.dp,
        defaultHeight = size.dp,
        viewportWidth = viewport,
        viewportHeight = viewport
    ).materialPath {
        moveTo(12f, 10f)
        lineTo(2f, 0f)
        lineTo(0f, 2f)
        lineTo(10f, 12f)
        lineTo(0f, 22f)
        lineTo(2f, 24f)
        lineTo(12f, 14f)
        lineTo(22f, 24f)
        lineTo(24f, 22f)
        lineTo(14f, 12f)
        lineTo(24f, 2f)
        lineTo(22f, 0f)
        lineTo(12f, 10f)
    }.build()

fun minIcon(size: Float = 24f, viewport: Float = 24f) =
    ImageVector.Builder(
        name = "",
        defaultWidth = size.dp,
        defaultHeight = size.dp,
        viewportWidth = viewport,
        viewportHeight = viewport
    ).materialPath {
        moveTo(2f, 14f)
        lineTo(22f, 14f)
        lineTo(22f, 10f)
        lineTo(2f, 10f)
        close()
    }.build()

fun maxIcon(size: Float = 24f, viewport: Float = 24f) =
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
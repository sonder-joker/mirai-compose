package com.youngerhousea.mirai.compose.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun RailTab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    selected: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    val color by animateColorAsState(if (selected) Color.Green else MaterialTheme.colors.primary)

    CompositionLocalProvider(LocalContentColor provides color) {
        Row(
            modifier = modifier.height(RailTabHeight).fillMaxWidth().clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            content = content
        )
    }
}

private val RailTabHeight = 80.dp
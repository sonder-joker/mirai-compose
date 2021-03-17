package com.youngerhousea.miraicompose.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun Spacer(currentBot: Float?, allBotMessage: Float?) {
    Column(
        Modifier
            .fillMaxWidth()
            .requiredHeight(80.dp)
            .background(MaterialTheme.colors.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "All: " + (allBotMessage?.toString()?.plus("msg/min") ?: "None"),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.subtitle2
            )
            Text(
                text = "Current: " + (currentBot?.toString()?.plus("msg/min") ?: "None"),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.subtitle2
            )
        }
        Divider(Modifier.height(1.dp).fillMaxWidth())
    }
}

@Composable
internal fun SelectEdgeText(text: String, isWishWindow: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .requiredHeight(80.dp)
            .background(if (isWishWindow) MaterialTheme.colors.background else MaterialTheme.colors.primary),
        contentAlignment = Alignment.Center
    ) {
        if (isWishWindow)
            Text(text, style = MaterialTheme.typography.subtitle1)
        else
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(text, style = MaterialTheme.typography.subtitle1)
            }
    }
}
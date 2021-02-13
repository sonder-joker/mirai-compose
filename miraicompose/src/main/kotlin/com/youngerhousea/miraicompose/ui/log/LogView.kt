package com.youngerhousea.miraicompose.ui.log

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.console.MiraiComposeLogger

@Composable
fun LogWindow() {
    LazyColumn(
        Modifier
            .fillMaxWidth()
    ) {
        items(MiraiComposeLogger.logs) {
            Text(
                it,
                modifier = Modifier
                    .padding(vertical = 5.dp)
            )
        }
    }
}


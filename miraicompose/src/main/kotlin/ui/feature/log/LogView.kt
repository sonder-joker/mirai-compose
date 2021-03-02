package com.youngerhousea.miraicompose.ui.feature.log

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.utils.Component


class Log(
    componentContext: ComponentContext,
    val loggerStorage: List<AnnotatedString>
) : Component, ComponentContext by componentContext{
    @Composable
    override fun render() {
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            items(loggerStorage) {
                Text(
                    it,
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                )
            }
        }
    }
}


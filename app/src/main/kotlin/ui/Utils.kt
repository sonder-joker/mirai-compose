package com.youngerhousea.mirai.compose.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


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
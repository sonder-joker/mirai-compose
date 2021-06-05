package com.youngerhousea.miraicompose.ui.bot

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.youngerhousea.miraicompose.component.bot.Login


@Composable
fun LoginUi(login: Login) {
    Children(login.state) { child ->
        child.instance()
    }
}





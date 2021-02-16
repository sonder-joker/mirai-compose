package com.youngerhousea.miraicompose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.youngerhousea.miraicompose.theme.ResourceImage

@Composable
fun AboutWindow(modifier: Modifier) {
    Column(modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(ResourceImage.mirai, "mirai")
        Text("Mirai Compose 2021!")
    }
}
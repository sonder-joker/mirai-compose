package com.youngerhousea.miraicompose.ui.feature.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.theme.ResourceImage

/**
 * 关于MiraiCompose的信息
 *
 */
class About(
    componentContext: ComponentContext
) : ComponentContext by componentContext {

}

@Composable
fun AboutUi(about: About) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(ResourceImage.mirai, "mirai")
        Text("Mirai Compose 2021!")
        Text("https://github.com/sonder-joker/mirai-compose")
        ExtendedFloatingActionButton(
            modifier = Modifier.padding(top = 50.dp),
            text = { Text("检查更新") },
            backgroundColor = Color.Gray,
            onClick = {
                //TODO: 获取更新
                // 我的想法是用github api取到最新release然后验证版本
            }
        )
    }

}
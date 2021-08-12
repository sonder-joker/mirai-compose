package com.youngerhousea.mirai.compose.ui.loginsolver

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import net.mamoe.mirai.utils.LoginSolver

/***
 *  解决PicCaptchaDialog的Dialog
 *
 *  @see LoginSolver
 */
@Composable
inline fun PicCaptchaDialog(data: ByteArray, id: Long, crossinline onExit: (picCaptcha: String?) -> Unit) {
    var picCaptcha by remember { mutableStateOf("") }
    Dialog(onCloseRequest = { onExit(picCaptcha) }) {
        PicCaptcha("Bot:$id")
    }
}

@Composable
fun PicCaptcha(title: String) {
    Scaffold(
        topBar = {
            Text(title)
        }
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("需要检测登录锁,手机扫码登录")
//            Image(image, null, Modifier.size(200.dp))
        }
    }
}

@Preview
@Composable
fun PicCaptchaPreview() {
    PicCaptcha("Bot:1234567890")
}
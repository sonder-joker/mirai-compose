package com.youngerhousea.mirai.compose.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment


@Composable
fun LoginSolverContent(
    title: String,
    tip: String,
    onFinish: (String) -> Unit,
    load: @Composable () -> Unit,
    refresh: () -> Unit,
    exit: () -> Unit
) {
    val (innerContent, setInnerContent) = remember(tip, title) { mutableStateOf("") }
    Scaffold(
        topBar = {
            Text(title)
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(tip)
            load()
            TextField(
                value = innerContent,
                onValueChange = setInnerContent
            )
            Row {
                Button({onFinish(innerContent)}) {
                    Text("Finish")
                }
                Button(refresh) {
                    Text("Refresh")
                }

                Button(exit) {
                    Text("Exit")
                }
            }
        }
    }
}


//@Preview
//@Composable
//fun LoginSolverContentPreview() {
//    LoginSolverContent(
//        "Bot:1234567890",
//        tip = "tip",
//        load = { QRCodeImage("test", qrCodeHeight = 200, qrCodeWidth = 200) },
//        value = "test",
//        onValueChange = {},
//        onFinish = {
//
//        },
//
//    )
//}
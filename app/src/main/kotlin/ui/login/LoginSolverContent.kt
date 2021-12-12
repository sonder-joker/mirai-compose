package com.youngerhousea.mirai.compose.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment


@Composable
fun LoginSolverContent(
    title: String,
    tip: String,
    value: String,
    onValueChange: (String) -> Unit,
    onFinish: () -> Unit,
    load: @Composable () -> Unit,
    refresh: () -> Unit,
    exit: () -> Unit
) {
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
                value = value,
                onValueChange = onValueChange
            )
            Row {
                Button(onFinish) {
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
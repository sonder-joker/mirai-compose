package com.youngerhousea.miraicompose.app.ui.bot

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.youngerhousea.miraicompose.core.component.bot.ReturnException
import com.youngerhousea.miraicompose.core.component.bot.SolvePicCaptcha
import com.youngerhousea.miraicompose.core.component.bot.SolveSliderCaptcha
import com.youngerhousea.miraicompose.core.component.bot.SolveUnsafeDeviceLoginVerify
import com.youngerhousea.miraicompose.app.utils.SkiaImageDecode

@Composable
fun SolvePicCaptchaUi(solvePicCaptcha: SolvePicCaptcha) {
    var value by mutableStateOf(TextFieldValue())
    Column {
        Text("Mirai PicCaptcha(${solvePicCaptcha.bot.id})")
        Image(SkiaImageDecode(solvePicCaptcha.data), null)
        TextField(value = value, onValueChange = { value = it })
        Row {
            Button(onClick = { solvePicCaptcha.result(null, ReturnException()) }) {
                Text("Exit")
            }
            Button(onClick = { solvePicCaptcha.result(value.text, null) }) {
                Text("Sure")
            }
        }
    }
}

@Composable
fun SolveSliderCaptchaUi(solveSliderCaptcha: SolveSliderCaptcha) {
    Column {
        Text("嵌入一下.jpg")
        Text(solveSliderCaptcha.url)
    }
}


@Composable
fun SolveUnsafeDeviceLoginVerifyUi(solveUnsafeDeviceLoginVerify: SolveUnsafeDeviceLoginVerify) = Box(
    Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    Column {
        Text("Mirai UnsafeDeviceLoginVerify(${solveUnsafeDeviceLoginVerify.bot.id}")

        SelectionContainer {
            Text(solveUnsafeDeviceLoginVerify.qrCodeUrl)
        }
        Row {
            Button(onClick = {
                solveUnsafeDeviceLoginVerify.result(null, null)
            }) {
                Text("Sure")
            }
            Button(onClick = {
                solveUnsafeDeviceLoginVerify.result(null, ReturnException())
            }) {
                Text("Return")
            }
        }
    }
}


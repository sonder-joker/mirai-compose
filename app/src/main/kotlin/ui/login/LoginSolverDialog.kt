package com.youngerhousea.mirai.compose.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.youngerhousea.mirai.compose.MiraiComposeDialog
import com.youngerhousea.mirai.compose.console.LoginSolverState
import com.youngerhousea.mirai.compose.ui.EmptyBot
import net.mamoe.mirai.utils.LoginSolver
import java.io.ByteArrayInputStream


/***
 *  解决PicCaptchaDialog的Dialog
 *
 *  @see LoginSolver
 */
@Composable
internal inline fun PicCaptchaDialog(
    loginSolverState: LoginSolverState.PicCaptcha,
    crossinline onExit: (picCaptcha: String?) -> Unit
) {
    val (picCaptcha, setPicCaptcha) = remember { mutableStateOf<String?>(null) }

    MiraiComposeDialog(onCloseRequest = { onExit(picCaptcha) }) {
        LoginSolverContent(
            title = "Bot:${loginSolverState.bot.id}",
            tip = "处理图片验证码",
            load = {  loadImageBitmap(ByteArrayInputStream(loginSolverState.data)) },
            value = picCaptcha,
            onValueChange = setPicCaptcha
        )
    }
}

@Composable
internal inline fun SliderCaptchaDialog(
    loginSolverState: LoginSolverState.SliderCaptcha,
    crossinline onExit: (picCaptcha: String?) -> Unit
) {
    val (sliderCaptcha, setSliderCaptcha) = remember { mutableStateOf<String?>(null) }
    MiraiComposeDialog(onCloseRequest = { onExit(sliderCaptcha) }) {
        LoginSolverContent(
            title = "Bot:${EmptyBot.id}",
            tip = "处理滑动验证码",
            value = sliderCaptcha,
            onValueChange = setSliderCaptcha,
            load = { QRCodeImageBitmap(loginSolverState.url, qrCodeHeight = 200, qrCodeWidth = 200) }
        )
    }
}

@Composable
internal inline fun UnsafeDeviceLoginVerifyDialog(
    loginSolverState: LoginSolverState.UnsafeDevice,
    crossinline onExit: (picCaptcha: String?) -> Unit
) {
    val (loginVerify, setLoginVerify) = remember { mutableStateOf<String?>(null) }
    MiraiComposeDialog(onCloseRequest = { onExit(loginVerify) }) {
        LoginSolverContent(
            title = "Bot:${EmptyBot.id}",
            tip = "处理不安全设备验证",
            value = loginVerify,
            onValueChange = setLoginVerify,
            load = { QRCodeImageBitmap(loginSolverState.url, qrCodeHeight = 200, qrCodeWidth = 200) }
        )
    }
}


//TODO : may be better
fun QRCodeImageBitmap(
    qrCodeData: String, qrCodeHeight: Int, qrCodeWidth: Int
): ImageBitmap {
    val matrix = MultiFormatWriter().encode(
        qrCodeData,
        BarcodeFormat.QR_CODE, qrCodeWidth, qrCodeHeight
    )
    return MatrixToImageWriter.toBufferedImage(matrix).toComposeImageBitmap()
}

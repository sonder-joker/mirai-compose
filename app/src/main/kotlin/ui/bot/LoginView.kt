package com.youngerhousea.miraicompose.app.ui.bot

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.youngerhousea.miraicompose.core.component.bot.*


@Composable
fun LoginUi(login: Login) {
    Children(login.state) { child ->
        when(val ch = child.instance) {
            is Login.Children.CInitLogin -> InitLoginUi(ch.initLogin)
            is Login.Children.CSolvePicCaptcha -> SolvePicCaptchaUi(ch.solvePicCaptcha)
            is Login.Children.CSolveSliderCaptcha -> SolveSliderCaptchaUi(ch.solveSliderCaptcha)
            is Login.Children.CSolveUnsafeDeviceLoginVerify -> SolveUnsafeDeviceLoginVerifyUi(ch.solveUnsafeDeviceLoginVerify)
        }
    }
}





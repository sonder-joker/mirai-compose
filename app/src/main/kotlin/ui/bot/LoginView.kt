package com.youngerhousea.miraicompose.app.ui.bot

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.youngerhousea.miraicompose.core.component.bot.*


@Composable
fun LoginUi(login: Login) {
    Children(login.state) { child ->
        when(val ch = child.instance) {
            is InitLogin -> InitLoginUi(ch)
            is SolvePicCaptcha -> SolvePicCaptchaUi(ch)
            is SolveSliderCaptcha -> SolveSliderCaptchaUi(ch)
            is SolveUnsafeDeviceLoginVerify -> SolveUnsafeDeviceLoginVerifyUi(ch)
        }
    }
}





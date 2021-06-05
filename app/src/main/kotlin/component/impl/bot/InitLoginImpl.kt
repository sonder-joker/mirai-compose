package com.youngerhousea.miraicompose.component.impl.bot

import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.component.bot.InitLogin
import com.youngerhousea.miraicompose.theme.R
import com.youngerhousea.miraicompose.utils.componentScope
import kotlinx.coroutines.*
import net.mamoe.mirai.network.*


class InitLoginImpl(
    componentContext: ComponentContext,
    private val onClick: suspend (account: Long, password: String) -> Unit,
) : InitLogin, ComponentContext by componentContext {
    private val scope = componentScope()

    private var _account by mutableStateOf(TextFieldValue())

    private var _password by mutableStateOf(TextFieldValue())

    private var _isLoading by mutableStateOf(false)

    override val account get() = _account

    override val password get() = _password

    override val isLoading get() = _isLoading

    override val state = SnackbarHostState()

    override fun onLogin() {
        _isLoading = true
        scope.launch {
            runCatching {
                withTimeout(20_000) {
                    onClick(_account.text.toLong(), _password.text)
                }
                if (state.showSnackbar("Loading", "Cancel") == SnackbarResult.ActionPerformed)
                    cancel()
            }.onFailure {
                val snackBarText = when (it) {
                    is WrongPasswordException -> {
                        R.String.wrongPassword
                    }
                    is RetryLaterException -> {
                        R.String.retryLater
                    }
                    is UnsupportedSliderCaptchaException -> {
                        R.String.unsupportedSliderCaptcha
                    }
                    is UnsupportedSMSLoginException -> {
                        R.String.unsupportedSMSLogin
                    }
                    is NoStandardInputForCaptchaException -> {
                        R.String.noStandardInputForCaptcha
                    }
                    is NoServerAvailableException -> {
                        R.String.noServerAvailable
                    }
                    is IllegalArgumentException -> {
                        R.String.passwordLengthMuch
                    }
                    is TimeoutCancellationException -> {
                        R.String.loginTimeOut
                    }
                    is CancellationException -> {
                        R.String.loginDismiss
                    }
                    else -> throw it
                }
                state.showSnackbar(snackBarText)
            }
        }.invokeOnCompletion {
            _isLoading = false
        }
    }

    override fun onAccountTextChange(textFieldValue: TextFieldValue) {
        _account = textFieldValue
    }

    override fun onPasswordTextChange(textFieldValue: TextFieldValue) {
        _password = textFieldValue
    }

}

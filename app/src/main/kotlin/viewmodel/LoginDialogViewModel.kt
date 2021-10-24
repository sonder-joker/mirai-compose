package com.youngerhousea.mirai.compose.viewmodel

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.youngerhousea.mirai.compose.console.ViewModelScope
import com.youngerhousea.mirai.compose.resource.R
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.network.*


interface Login {
    val state: State<LoginState>

    fun dispatch(loginAction: LoginAction);
}

class LoginViewModel : Login, ViewModelScope() {
    override val state: MutableState<LoginState> = mutableStateOf(LoginState())

    lateinit var loginJob: Job

    override fun dispatch(loginAction: LoginAction) {
        viewModelScope.launch {
            state.value = reduce(state.value, loginAction)
        }
    }

    private suspend fun reduce(state: LoginState, loginAction: LoginAction): LoginState {
        return when (loginAction) {
            is LoginAction.InputAccount -> state.copy(account = loginAction.account)
            is LoginAction.InputPassword -> state.copy(password = loginAction.password)
            is LoginAction.Login ->
                try {
                    loginJob = login(state.account, state.password)
                    state.copy(account = "", password = "")
                } catch (e: Exception) {
                    when (e) {
                        is WrongPasswordException -> {
                            reduceLoginException(state, e.message ?: R.String.PasswordError)
                        }
                        is RetryLaterException -> {
                            reduceLoginException(state, e.message ?: R.String.RetryLater)
                        }
                        is UnsupportedSliderCaptchaException -> {
                            reduceLoginException(state, e.message ?: R.String.NotSupportSlider)
                        }
                        is UnsupportedSMSLoginException -> {
                            reduceLoginException(state, e.message ?: R.String.SMSLoginError)
                        }
                        is NoStandardInputForCaptchaException -> {
                            reduceLoginException(state, e.message ?: R.String.NoStandInput)
                        }
                        is NoServerAvailableException -> {
                            reduceLoginException(state, e.message ?: R.String.NoServerError)
                        }
                        is IllegalArgumentException -> {
                            reduceLoginException(state, e.message ?: R.String.IllPassword)
                        }
                        is CancellationException -> {
                            reduceLoginException(state, e.message ?: R.String.CancelLogin)
                        }
                        else -> {
                            reduceLoginException(state, e.message ?: R.String.UnknownError)
                        }
                    }
                }
            is LoginAction.CancelLogin -> {
                if (::loginJob.isInitialized) {
                    loginJob.cancel()
                }
                return state
            }
        }
    }

    private suspend fun reduceLoginException(state: LoginState, message: String): LoginState {
        state.host.showSnackbar(message)
        return state.copy(isLoading = false)
    }

    private fun login(account: String, password: String): Job {
        return viewModelScope.launch {
            MiraiConsole.addBot(account.toLong(), password).alsoLogin()
        }
    }
}


sealed interface LoginAction {
    class InputAccount(val account: String) : LoginAction
    class InputPassword(val password: String) : LoginAction
    object CancelLogin : LoginAction
    object Login : LoginAction
}

@Immutable
data class LoginState(
    val account: String = "",
    val password: String = "",
    val host: SnackbarHostState = SnackbarHostState(),
    val isLoading: Boolean = false
)



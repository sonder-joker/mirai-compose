package com.youngerhousea.mirai.compose.viewmodel

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.youngerhousea.mirai.compose.console.LoginSolverState
import com.youngerhousea.mirai.compose.console.MiraiComposeLoginSolver
import com.youngerhousea.mirai.compose.console.UICannotFinish
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

    val solver: MiraiComposeLoginSolver

    fun dispatch(loginAction: LoginAction)
}

class LoginViewModel : Login, ViewModelScope() {
    override val state: MutableState<LoginState> = mutableStateOf(LoginState())

    override val solver: MiraiComposeLoginSolver = MiraiComposeLoginSolver {
        state.value = state.value.copy(solver = it)
    }

    lateinit var loginJob: Job

    override fun dispatch(loginAction: LoginAction) {
        loginJob = viewModelScope.launch {
            when (loginAction) {
                is LoginAction.TryLogin -> {
                    try {
                        state.value = state.value.copy(isLoading = true)
                        MiraiConsole.addBot(loginAction.account.toLong(), loginAction.password).alsoLogin()
                    } catch (e: Exception) {
                        reduceLoginException(state.value, e)
                    } finally {
                        state.value = state.value.copy(isLoading = false)
                    }
                    state.value = state.value.copy(success = true, isOpen = false)
                }
                is LoginAction.CancelLogin -> {
                    if (::loginJob.isInitialized && loginJob.isActive) {
                        loginJob.cancel()
                    }
                }
                LoginAction.Exit -> {
                    when (state.value.solver) {
                        LoginSolverState.Nothing -> {}
                        is LoginSolverState.PicCaptcha -> solver.exitPicCaptcha()
                        is LoginSolverState.SliderCaptcha -> solver.exitSliderCaptcha()
                        is LoginSolverState.UnsafeDevice -> solver.exitUnsafeDeviceLoginVerify()
                    }
                }
                LoginAction.Refresh -> {
                    when (state.value.solver) {
                        LoginSolverState.Nothing -> {}
                        is LoginSolverState.PicCaptcha -> solver.refreshCaptcha()
                        is LoginSolverState.SliderCaptcha -> solver.refreshSliderCaptcha()
                        is LoginSolverState.UnsafeDevice -> solver.refreshUnsafeDeviceLoginVerify()
                    }
                }

                is LoginAction.Solve -> {
                    when (state.value.solver) {
                        LoginSolverState.Nothing -> {}
                        is LoginSolverState.PicCaptcha -> solver.solvePicCaptcha(state.value.captcha)
                        is LoginSolverState.SliderCaptcha -> solver.solveSliderCaptcha(state.value.slider)
                        is LoginSolverState.UnsafeDevice -> solver.solveUnsafeDeviceLoginVerify(state.value.unsafe)
                    }
                }
                is LoginAction.Update -> {
                    state.value = when (state.value.solver) {
                        LoginSolverState.Nothing -> state.value
                        is LoginSolverState.PicCaptcha -> state.value.copy(captcha = loginAction.value)
                        is LoginSolverState.SliderCaptcha -> state.value.copy(captcha = loginAction.value)
                        is LoginSolverState.UnsafeDevice -> state.value.copy(captcha = loginAction.value)
                    }
                }
                LoginAction.Reset -> state.value = LoginState()
                LoginAction.Open -> state.value = state.value.copy(isOpen = true)
                LoginAction.Close -> state.value = state.value.copy(isOpen = false)
            }
        }
    }

    private suspend fun reduceLoginException(state: LoginState, e: Exception): LoginState {
        when (e) {
            is WrongPasswordException -> {
                state.host.showSnackbar(e.message ?: R.String.PasswordError)
            }
            is RetryLaterException -> {
                state.host.showSnackbar(e.message ?: R.String.RetryLater)
            }
            is UnsupportedSliderCaptchaException -> {
                state.host.showSnackbar(e.message ?: R.String.NotSupportSlider)
            }
            is UnsupportedSMSLoginException -> {
                state.host.showSnackbar(e.message ?: R.String.SMSLoginError)
            }
            is NoStandardInputForCaptchaException -> {
                state.host.showSnackbar(e.message ?: R.String.NoStandInput)
            }
            is NoServerAvailableException -> {
                state.host.showSnackbar(e.message ?: R.String.NoServerError)
            }
            is IllegalArgumentException -> {
                state.host.showSnackbar(e.message ?: R.String.IllPassword)
            }
            is CancellationException -> {
                state.host.showSnackbar(e.message ?: R.String.CancelLogin)
            }
            is UICannotFinish -> {

            }
            else -> {
                state.host.showSnackbar(e.message ?: R.String.UnknownError)
            }
        }

        return state.copy(isLoading = false)
    }
}


sealed interface LoginAction {
    object CancelLogin : LoginAction
    class TryLogin(val account: String, val password: String) : LoginAction
    class Update(val value: String) : LoginAction
    object Refresh : LoginAction
    // Control solver
    object Reset : LoginAction
    object Solve : LoginAction
    // Exit login solver state
    object Exit : LoginAction

    // Control windows
    object Open : LoginAction
    object Close : LoginAction
}

@Immutable
data class LoginState(
    val host: SnackbarHostState = SnackbarHostState(),
    val isLoading: Boolean = false,
    val captcha: String = "",
    val slider: String = "",
    val unsafe: String = "",
    val solver: LoginSolverState = LoginSolverState.Nothing,
    val success: Boolean = false,
    val isOpen: Boolean = false
)



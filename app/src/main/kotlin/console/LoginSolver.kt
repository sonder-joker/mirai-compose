@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package com.youngerhousea.mirai.compose.console

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CompletableDeferred
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.network.CustomLoginFailedException
import net.mamoe.mirai.utils.LoginSolver

interface Solver {

    enum class Kind {
        Pic, // bytearray
        Slider, // url
        Unsafe // url
    }

    data class SolverState(
        val bot: Bot,
        val data: String,
        val title: String,
        val kind: Kind,
        val verifyData: CompletableDeferred<String?>,
        val dialogIsOpen: Boolean
    )

    val solverState: State<SolverState?>

    fun dispatch(action: Action)

    sealed class Action {
        class CompleteVerify(val input: String) : Action()
        object RefreshData : Action()
        object ExitProcessSolver : Action()
    }
}

object MiraiComposeLoginSolver : LoginSolver(), Solver {
    override val solverState: MutableState<Solver.SolverState?> = mutableStateOf(null)

    override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? {
        MiraiConsole.mainLogger.info("Start load pic captcha")
        solverState.value =
            Solver.SolverState(bot, String(data), "处理图片验证码", Solver.Kind.Pic, CompletableDeferred(), true)
        try {
            return solverState.value!!.verifyData.await()
        } catch (e: Exception) {
            MiraiConsole.mainLogger.info("Load pic captcha failed")
            throw e
        } finally {
            solverState.value = null
        }
    }

    override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? {
        MiraiConsole.mainLogger.info("Start load slider captcha")
        solverState.value = Solver.SolverState(bot, url, "处理滑动验证码", Solver.Kind.Slider, CompletableDeferred(), true)
        try {
            return solverState.value!!.verifyData.await()
        } catch (e: Exception) {
            MiraiConsole.mainLogger.info("Load slider captcha failed")
            throw e
        } finally {
            solverState.value = null
        }
    }

    override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? {
        MiraiConsole.mainLogger.info("Start load unsafe device")
        solverState.value = Solver.SolverState(bot, url, "处理不安全验证码", Solver.Kind.Unsafe, CompletableDeferred(), true)
        try {
            return solverState.value!!.verifyData.await()
        } catch (e: Exception) {
            MiraiConsole.mainLogger.info("Load unsafe device failed")
            throw e
        } finally {
            solverState.value = null
        }
    }

    override fun dispatch(action: Solver.Action) {
        solverState.value?.let {
            when (action) {
                is Solver.Action.CompleteVerify -> it.verifyData.complete(action.input)
                Solver.Action.ExitProcessSolver -> it.verifyData.completeExceptionally(UICannotFinish())
                Solver.Action.RefreshData -> it.verifyData.complete(null)
            }
        }
    }
}

class UICannotFinish : CustomLoginFailedException(killBot = true, message = "UI cannot finish login")
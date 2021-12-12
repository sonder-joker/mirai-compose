@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package com.youngerhousea.mirai.compose.console

import kotlinx.coroutines.CompletableDeferred
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.extensions.BotConfigurationAlterer
import net.mamoe.mirai.console.internal.extension.GlobalComponentStorage
import net.mamoe.mirai.console.rootDir
import net.mamoe.mirai.console.util.CoroutineScopeUtils.childScopeContext
import net.mamoe.mirai.network.CustomLoginFailedException
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.LoginSolver
import net.mamoe.mirai.utils.MiraiLogger
import net.mamoe.mirai.utils.verbose

sealed class LoginSolverState {
    object Nothing : LoginSolverState()
    class PicCaptcha(
        val bot: Bot,
        val data: ByteArray
    ) : LoginSolverState()

    class SliderCaptcha(
        val bot: Bot,
        val url: String
    ) : LoginSolverState()

    class UnsafeDevice(
        val bot: Bot,
        val url: String
    ) : LoginSolverState()
}


@Suppress("unused")
fun MiraiConsoleImplementation.addBot(id: Long, password: String, solver: MiraiComposeLoginSolver): Bot =
    addBotImpl(id, password, solver)

@Suppress("unused")
fun MiraiConsoleImplementation.addBot(id: Long, password: ByteArray, solver: MiraiComposeLoginSolver): Bot =
    addBotImpl(id, password, solver)

@Suppress("UNREACHABLE_CODE")
private fun addBotImpl(id: Long, password: Any, solver: MiraiComposeLoginSolver): Bot {
    var config = BotConfiguration().apply {

        workingDir = MiraiConsole.rootDir
            .resolve("bots")
            .resolve(id.toString())
            .also { it.mkdirs() }

        MiraiConsole.mainLogger.verbose { "Bot $id working in $workingDir" }

        val deviceInRoot = MiraiConsole.rootDir.resolve("device.json")
        val deviceInWorkingDir = workingDir.resolve("device.json")

        val deviceInfoInWorkingDir = workingDir.resolve("deviceInfo.json")
        if (!deviceInWorkingDir.exists()) {
            when {
                deviceInfoInWorkingDir.exists() -> {
                    // rename bots/id/deviceInfo.json to bots/id/device.json
                    MiraiConsole.mainLogger.verbose { "Renaming $deviceInfoInWorkingDir to $deviceInWorkingDir" }
                    deviceInfoInWorkingDir.renameTo(deviceInWorkingDir)
                }
                deviceInRoot.exists() -> {
                    // copy root/device.json to bots/id/device.json
                    MiraiConsole.mainLogger.verbose { "Coping $deviceInRoot to $deviceInWorkingDir" }
                    deviceInRoot.copyTo(deviceInWorkingDir)
                }
            }
        }

        fileBasedDeviceInfo("device.json")

        redirectNetworkLogToDirectory()
        this.botLoggerSupplier = {
            MiraiLogger.Factory.create(Bot::class, "Bot.${it.id}")
        }
        parentCoroutineContext = MiraiConsole.childScopeContext("Bot $id")
        autoReconnectOnForceOffline()

        this.loginSolver = solver
    }

    config = GlobalComponentStorage.run {
        BotConfigurationAlterer.foldExtensions(config) { acc, extension ->
            extension.alterConfiguration(id, acc)
        }
    }

    return when (password) {
        is ByteArray -> BotFactory.newBot(id, password, config)
        is String -> BotFactory.newBot(id, password, config)
        else -> throw IllegalArgumentException("Bad password type: `${password.javaClass.name}`. Require ByteArray or String")
    }
}


class MiraiComposeLoginSolver(
    val set: (LoginSolverState) -> Unit
) : LoginSolver() {

    private val picCaptcha = CompletableDeferred<String?>()

    private val sliderCaptcha = CompletableDeferred<String?>()

    private val unsafeDeviceLoginVerify = CompletableDeferred<String?>()

    override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? {
        set(LoginSolverState.PicCaptcha(bot, data))
        try {
            return picCaptcha.await()
        } catch (e: Exception) {
            set(LoginSolverState.Nothing)
            throw e
        } finally {
            set(LoginSolverState.Nothing)
        }
    }

    override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? {
        set(LoginSolverState.SliderCaptcha(bot, url))
        try {
            return sliderCaptcha.await()
        } catch (e: Exception) {
            set(LoginSolverState.Nothing)
            throw e
        } finally {
            set(LoginSolverState.Nothing)
        }
    }

    override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? {
        set(LoginSolverState.UnsafeDevice(bot, url))
        try {
            return unsafeDeviceLoginVerify.await()
        } catch (e: Exception) {
            set(LoginSolverState.Nothing)
            throw e
        } finally {
            set(LoginSolverState.Nothing)
        }
    }

    fun solvePicCaptcha(value: String) {
        picCaptcha.complete(value)
    }

    fun refreshCaptcha() {
        picCaptcha.complete(null)
    }

    fun exitPicCaptcha() {
        picCaptcha.completeExceptionally(UICannotFinish())
    }

    fun solveSliderCaptcha(value: String) {
        sliderCaptcha.complete(value)
    }

    fun refreshSliderCaptcha() {
        sliderCaptcha.complete(null)
    }

    fun exitSliderCaptcha() {
        sliderCaptcha.completeExceptionally(UICannotFinish())
    }

    fun solveUnsafeDeviceLoginVerify(value: String) {
        sliderCaptcha.complete(value)
    }

    fun refreshUnsafeDeviceLoginVerify() {
        sliderCaptcha.complete(null)
    }

    fun exitUnsafeDeviceLoginVerify() {
        unsafeDeviceLoginVerify.completeExceptionally(UICannotFinish())
    }
}


class UICannotFinish : CustomLoginFailedException(killBot = true, message = "UI cannot finish login") {

}
package com.youngerhousea.mirai.compose.console

import androidx.compose.runtime.State
import com.youngerhousea.mirai.compose.console.impl.Lifecycle
import com.youngerhousea.mirai.compose.console.impl.LifecycleOwner
import com.youngerhousea.mirai.compose.console.impl.ReadablePluginConfigStorage
import com.youngerhousea.mirai.compose.console.impl.ReadablePluginDataStorage
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.data.PluginConfig
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.plugin.center.PluginCenter
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.console.util.ConsoleInternalApi
import net.mamoe.mirai.console.util.SemVersion
import net.mamoe.mirai.utils.MiraiLogger
import java.time.Instant


interface MiraiComposeImplementation :
    MiraiComposeInternal,
    MiraiConsoleImplementation,
    ViewModelStoreOwner,
    LifecycleOwner {
    override val configStorageForBuiltIns: ReadablePluginConfigStorage
    override val configStorageForJvmPluginLoader: ReadablePluginConfigStorage
    override val dataStorageForBuiltIns: ReadablePluginDataStorage
    override val dataStorageForJvmPluginLoader: ReadablePluginDataStorage
}


private object MiraiComposeBridge : MiraiCompose,
    MiraiComposeImplementation by (MiraiConsoleImplementation.getInstance() as MiraiComposeImplementation) {

    override val buildDate: Instant get() = MiraiConsole.buildDate

    @ConsoleInternalApi
    override val mainLogger: MiraiLogger
        get() = MiraiConsole.mainLogger

    @ConsoleExperimentalApi
    override val pluginCenter: PluginCenter
        get() = MiraiConsole.pluginCenter

    override val version: SemVersion get() = MiraiConsole.version
}

interface MiraiCompose : MiraiConsole {
    //TODO: May shouldn't call it in viewModel directly?
    val lifecycle: Lifecycle

    companion object INSTANCE : MiraiCompose, MiraiConsole by MiraiComposeBridge {
        override val lifecycle: Lifecycle get() = MiraiComposeBridge.lifecycle
    }
}


// need a more normal way
interface MiraiComposeInternal {
    val JvmPlugin.data: List<PluginData>

    val JvmPlugin.config: List<PluginConfig>

    val loginSolverState: State<LoginSolverState>

    fun dispatch(login: Login)

    fun cancel()
}

sealed class Login {
    class PicCaptcha(val string: String?) : Login()
    class SliderCaptcha(val string: String?) : Login()
    class UnsafeDevice(val string: String?) : Login()
}

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

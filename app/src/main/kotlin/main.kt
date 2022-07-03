import net.mamoe.mirai.console.MiraiConsoleFrontEndDescription
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.data.PluginDataStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginLoader
import net.mamoe.mirai.console.plugin.loader.PluginLoader
import net.mamoe.mirai.console.util.ConsoleInput
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.LoginSolver
import net.mamoe.mirai.utils.MiraiLogger
import java.nio.file.Path
import kotlin.coroutines.CoroutineContext

class MiraiCompose : MiraiConsoleImplementation {
    override val builtInPluginLoaders: List<Lazy<PluginLoader<*, *>>> = listOf()
    override val commandManager: CommandManager
        get() = TODO("Not yet implemented")
    override val configStorageForBuiltIns: PluginDataStorage
        get() = TODO("Not yet implemented")
    override val configStorageForJvmPluginLoader: PluginDataStorage
        get() = TODO("Not yet implemented")
    override val consoleCommandSender: MiraiConsoleImplementation.ConsoleCommandSenderImpl
        get() = TODO("Not yet implemented")
    override val consoleDataScope: MiraiConsoleImplementation.ConsoleDataScope
        get() = TODO("Not yet implemented")
    override val consoleInput: ConsoleInput
        get() = TODO("Not yet implemented")
    override val coroutineContext: CoroutineContext
        get() = TODO("Not yet implemented")
    override val dataStorageForBuiltIns: PluginDataStorage
        get() = TODO("Not yet implemented")
    override val dataStorageForJvmPluginLoader: PluginDataStorage
        get() = TODO("Not yet implemented")
    override val frontEndDescription: MiraiConsoleFrontEndDescription
        get() = TODO("Not yet implemented")
    override val jvmPluginLoader: JvmPluginLoader
        get() = TODO("Not yet implemented")
    override val rootPath: Path
        get() = TODO("Not yet implemented")

    override fun createLogger(identity: String?): MiraiLogger {
        TODO("Not yet implemented")
    }

    override fun createLoginSolver(requesterBot: Long, configuration: BotConfiguration): LoginSolver {
        TODO("Not yet implemented")
    }

}
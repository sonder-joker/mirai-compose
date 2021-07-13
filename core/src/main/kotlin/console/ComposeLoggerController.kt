package com.youngerhousea.miraicompose.core.console

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value
import net.mamoe.mirai.console.logging.AbstractLoggerController

internal object ComposeLoggerController : AbstractLoggerController.PathBased() {
    var initialized = false

    override fun findPriority(identity: String?): LogPriority? {
        if (!initialized) return LogPriority.NONE
        return if (identity == null) {
            LoggerConfig.defaultPriority
        } else {
            LoggerConfig.loggers[identity]
        }
    }

    override val defaultPriority: LogPriority
        get() = if (initialized) LoggerConfig.defaultPriority else LogPriority.NONE
}

internal object LoggerConfig : AutoSavePluginConfig("Logger") {
    @ValueDescription(
        """
        日志输出等级 可选值: ALL, VERBOSE, DEBUG, INFO, WARNING, ERROR, NONE
    """
    )
    var defaultPriority by value(AbstractLoggerController.LogPriority.INFO)

    @ValueDescription(
        """
        特定日志记录器输出等级
    """
    )
    val loggers: Map<String, AbstractLoggerController.LogPriority> by value(
        mapOf(
            "example.logger" to AbstractLoggerController.LogPriority.NONE,
            "console.debug" to AbstractLoggerController.LogPriority.NONE,
            "Bot" to AbstractLoggerController.LogPriority.ALL,
        )
    )
}
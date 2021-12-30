package com.youngerhousea.mirai.compose.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.youngerhousea.mirai.compose.console.ViewModelScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.console.command.descriptor.AbstractCommandValueParameter
import net.mamoe.mirai.console.command.descriptor.CommandReceiverParameter
import net.mamoe.mirai.console.command.descriptor.CommandValueParameter
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.command.parse.CommandCall
import net.mamoe.mirai.console.command.parse.CommandValueArgument
import net.mamoe.mirai.console.util.ConsoleInternalApi
import net.mamoe.mirai.console.util.cast
import net.mamoe.mirai.console.util.safeCast
import net.mamoe.mirai.utils.MiraiLogger
import net.mamoe.mirai.utils.warning
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf


/**
 * Compose的所有日志
 *
 */
interface ConsoleLog {

    val state: State<InnerState>

    fun dispatch(action: Action)

    sealed interface Action {
        class UpdateSearchContent(val content: String) : Action
        class UpdateCurrentCommand(val content: String) : Action
        object EnterCommand : Action
        object SetSearchBar : Action
    }

    data class InnerState(
        val searchContent: String = "",
        val currentCommand: String = "",
        val searchBarVisible: Boolean = false
    )
}



class ConsoleLogViewModel @OptIn(ConsoleInternalApi::class) constructor(
    private val logger: MiraiLogger = MiraiConsole.mainLogger,
) : ConsoleLog, ViewModelScope() {
    override val state: MutableState<ConsoleLog.InnerState> = mutableStateOf(ConsoleLog.InnerState())

    override fun dispatch(action: ConsoleLog.Action) {
        viewModelScope.launch {
            state.value = reduce(state.value, action)
        }
    }

    private suspend fun reduce(value: ConsoleLog.InnerState, action: ConsoleLog.Action): ConsoleLog.InnerState {
        return when (action) {
            is ConsoleLog.Action.UpdateSearchContent -> value.copy(searchContent = action.content)
            is ConsoleLog.Action.UpdateCurrentCommand -> value.copy(currentCommand = action.content)
            is ConsoleLog.Action.EnterCommand -> {
                SolveCommandResult(value.currentCommand, logger)
                return value.copy(currentCommand = "")
            }
            is ConsoleLog.Action.SetSearchBar -> value.copy(searchBarVisible = !value.searchBarVisible)
        }
    }

}

//@OptIn(ExperimentalCommandDescriptors::class)
//private suspend fun CommandPrompt(
//    currentCommand: String,
//): String {
//    return when (val result = ConsoleCommandSender.executeCommand(currentCommand)) {
//        is CommandExecuteResult.UnmatchedSignature -> {
//            result.failureReasons.render(result.command, result.call)
//        }
//        else -> ""
//    }
//}

@OptIn(ExperimentalCommandDescriptors::class)
private suspend fun SolveCommandResult(
    currentCommand: String,
    logger: MiraiLogger
) {
    when (val result = ConsoleCommandSender.executeCommand(currentCommand)) {
        is CommandExecuteResult.Success -> {
        }
        is CommandExecuteResult.IllegalArgument -> {
            val message = result.exception.message
            if (message != null) {
                logger.warning(message)
            } else logger.warning(result.exception)
        }
        is CommandExecuteResult.ExecutionFailed -> {
            logger.error(result.exception)
        }
        is CommandExecuteResult.UnresolvedCommand -> {
            logger.warning { "未知指令: ${currentCommand}, 输入 /help 获取帮助" }
        }
        is CommandExecuteResult.PermissionDenied -> {
            logger.warning { "权限不足." }
        }
        is CommandExecuteResult.UnmatchedSignature -> {
            logger.warning { "参数不匹配, 你是否想执行: \n" + result.failureReasons.render(result.command, result.call) }
        }
        is CommandExecuteResult.Failure -> {
            logger.warning { result.toString() }
        }
    }
}


@OptIn(ExperimentalCommandDescriptors::class)
private fun List<UnmatchedCommandSignature>.render(command: Command, call: CommandCall): String {
    val list =
        this.filter lambda@{ signature ->
            if (signature.failureReason.safeCast<FailureReason.InapplicableValueArgument>()?.parameter is AbstractCommandValueParameter.StringConstant) return@lambda false
            if (signature.signature.valueParameters.anyStringConstantUnmatched(call.valueArguments)) return@lambda false
            true
        }
    if (list.isEmpty()) {
        return command.usage
    }
    return list.joinToString("\n") { it.render(command) }
}

@OptIn(ExperimentalCommandDescriptors::class)
private fun List<CommandValueParameter<*>>.anyStringConstantUnmatched(arguments: List<CommandValueArgument>): Boolean {
    return this.zip(arguments).any { (parameter, argument) ->
        parameter is AbstractCommandValueParameter.StringConstant && !parameter.accepts(argument, null)
    }
}

@OptIn(ExperimentalCommandDescriptors::class)
internal fun UnmatchedCommandSignature.render(command: Command): String {
    @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
    val usage =
        net.mamoe.mirai.console.internal.command.CommandReflector.generateUsage(
            command,
            null,
            listOf(this.signature)
        )
    return usage.trim() + "    (${failureReason.render()})"
}

@OptIn(ExperimentalCommandDescriptors::class)
internal fun FailureReason.render(): String {
    return when (this) {
        is FailureReason.InapplicableArgument -> "参数类型错误"
        is FailureReason.InapplicableReceiverArgument -> "需要由 ${this.parameter.renderAsName()} 执行"
        is FailureReason.TooManyArguments -> "参数过多"
        is FailureReason.NotEnoughArguments -> "参数不足"
        is FailureReason.ResolutionAmbiguity -> "调用歧义"
        is FailureReason.ArgumentLengthMismatch -> {
            // should not happen, render it anyway.
            "参数长度不匹配"
        }
    }
}

@OptIn(ExperimentalCommandDescriptors::class)
internal fun CommandReceiverParameter<*>.renderAsName(): String {
    val classifier = this.type.classifier.cast<KClass<out CommandSender>>()
    return when {
        classifier.isSubclassOf(ConsoleCommandSender::class) -> "控制台"
        classifier.isSubclassOf(FriendCommandSenderOnMessage::class) -> "好友私聊"
        classifier.isSubclassOf(FriendCommandSender::class) -> "好友"
        classifier.isSubclassOf(MemberCommandSenderOnMessage::class) -> "群内发言"
        classifier.isSubclassOf(MemberCommandSender::class) -> "群成员"
        classifier.isSubclassOf(GroupTempCommandSenderOnMessage::class) -> "群临时会话"
        classifier.isSubclassOf(GroupTempCommandSender::class) -> "群临时好友"
        classifier.isSubclassOf(UserCommandSender::class) -> "用户"
        else -> classifier.simpleName ?: classifier.toString()
    }
}
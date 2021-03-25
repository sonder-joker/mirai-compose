package com.youngerhousea.miraicompose.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.shortcuts
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.utils.VerticalScrollbar
import com.youngerhousea.miraicompose.utils.chunked
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.console.command.descriptor.AbstractCommandValueParameter
import net.mamoe.mirai.console.command.descriptor.CommandReceiverParameter
import net.mamoe.mirai.console.command.descriptor.CommandValueParameter
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.command.parse.CommandCall
import net.mamoe.mirai.console.command.parse.CommandValueArgument
import net.mamoe.mirai.console.util.cast
import net.mamoe.mirai.console.util.safeCast
import net.mamoe.mirai.utils.MiraiLogger
import net.mamoe.mirai.utils.warning
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf


@Composable
internal fun LogBox(modifier: Modifier = Modifier, logs: List<AnnotatedString>) {
    BoxWithConstraints(modifier) {
        val listLog = logs.flatMap {
            it.chunked(constraints.maxWidth / 9)
        }
        val itemHeight = 40.dp
        val state = remember(listLog) { LazyListState() }

        LazyColumn(
            Modifier
                .fillMaxWidth(),
            state = state
        ) {
            items(
                listLog
            ) {
                //TODO:HapticFeedback.performHapticFeedback not implemented yet
                SelectionContainer {
                    Text(
                        it,
                        modifier = Modifier
                            .height(itemHeight)
                    )
                }
            }
        }

        VerticalScrollbar(
            Modifier.align(Alignment.CenterEnd),
            state,
            listLog.size,
            itemHeight
        )

        LaunchedEffect(listLog) {
            if (listLog.isNotEmpty())
                state.scrollToItem(listLog.size - 1, 0)
        }
    }
}

@OptIn(ExperimentalCommandDescriptors::class)
@Composable
internal fun CommandSendBox(logger: MiraiLogger, modifier: Modifier = Modifier) {
    var currentCommand by remember(logger) { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    fun onClick() {
        scope.launch {
            try {
                SolveCommandResult(currentCommand, logger)
            } catch (e :Exception) {

            } finally {
                currentCommand = ""
            }
        }
    }
    Row(modifier) {
        TextField(
            currentCommand,
            onValueChange = {
                currentCommand = it
            },
            modifier = Modifier
                .weight(13f)
                .shortcuts {
                    on(Key.Enter, callback = ::onClick)
                },
            singleLine = true,
        )

        Spacer(
            Modifier.weight(1f)
        )

        FloatingActionButton(
            onClick = ::onClick,
            modifier = Modifier
                .weight(2f),
            backgroundColor = MaterialTheme.colors.background,
        ) {
            Text("发送")
        }
    }

}


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
            logger.warning { "未知指令: ${currentCommand}, 输入 ? 获取帮助" }
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
        net.mamoe.mirai.console.internal.command.CommandReflector.generateUsage(command, null, listOf(this.signature))
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
package com.youngerhousea.miraicompose.ui.common

import androidx.compose.animation.animateContentSize
import androidx.compose.desktop.AppManager
import androidx.compose.desktop.AppWindow
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.plus
import androidx.compose.ui.input.key.shortcuts
import com.youngerhousea.miraicompose.console.ComposeLog
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
internal fun LogBox(modifier: Modifier = Modifier, logs: List<ComposeLog>) {
    var isShowSearch by remember { mutableStateOf(true) }
    val lazyListState = rememberLazyListState()
    var searchText by remember { mutableStateOf("") }

    Box(modifier) {
        LazyColumn(state = lazyListState, modifier = Modifier.animateContentSize()) {
            stickyHeader {
                if (isShowSearch)
                    OutlinedTextField(searchText, { searchText = it })
            }

            items(logs) { adaptiveLog ->
                SelectionContainer {
                    Text(adaptiveLog.parseInSearch(searchText))
                }
            }
        }
    }

    DisposableEffect(Unit) {
        AppManager.windows.first().let {
            (it as AppWindow).keyboard.setShortcut(Key.CtrlLeft + Key.F) {
                isShowSearch = !isShowSearch
            }
        }
        onDispose {
            AppManager.windows.first().let {
                (it as AppWindow).keyboard.removeShortcut(Key.CtrlLeft + Key.F)
            }
        }
    }
    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty())
            lazyListState.animateScrollToItem(logs.size)
    }
}

@Composable
internal fun CommandSendBox(logger: MiraiLogger, modifier: Modifier = Modifier) {
    var currentCommand by remember(logger) { mutableStateOf("") }
    var re by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val onClick: () -> Unit = {
        scope.launch {
//            try {
            SolveCommandResult(currentCommand, logger)
//            } catch (e: Exception) {
//
//            } finally {
            currentCommand = ""
//            }
        }
    }

    Row(modifier) {
        OutlinedTextField(
            currentCommand,
            onValueChange = {
                currentCommand = it
            },
            modifier = Modifier
                .weight(13f)
                .shortcuts {
                    on(Key.Enter, callback = onClick)
                },
            singleLine = true,
        )

        Spacer(
            Modifier.weight(1f)
        )

        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .weight(2f),
        ) {
            Text("Send")
        }
    }
}

/**
 * TODO:用于自动补全
 *
 */
@OptIn(ExperimentalCommandDescriptors::class)
private suspend fun CommandPrompt(
    currentCommand: String,
): String {
    return when (val result = ConsoleCommandSender.executeCommand(currentCommand)) {
        is CommandExecuteResult.UnmatchedSignature -> {
            result.failureReasons.render(result.command, result.call)
        }
        else -> ""
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
package com.youngerhousea.miraicompose.app.ui.shared

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.shortcuts
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.youngerhousea.miraicompose.app.theme.AppTheme
import com.youngerhousea.miraicompose.core.console.Log
import com.youngerhousea.miraicompose.core.console.LogPriority
import com.youngerhousea.miraicompose.core.console.original
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
import java.util.regex.PatternSyntaxException
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

val Log.color: Color
    get() = when (first) {
        LogPriority.VERBOSE -> AppTheme.logColors.verbose
        LogPriority.INFO -> AppTheme.logColors.info
        LogPriority.WARNING -> AppTheme.logColors.warning
        LogPriority.ERROR -> AppTheme.logColors.error
        LogPriority.DEBUG -> AppTheme.logColors.debug
    }


val Log.composeLog
    get(): AnnotatedString =
        buildAnnotatedString {
            pushStyle(SpanStyle(color))
            append(original)
        }


fun Log.parseInSearch(searchText: String): AnnotatedString {
    if (searchText.isEmpty()) return composeLog
    val builder = AnnotatedString.Builder()
    try {
        original.split("((?<=${searchText})|(?=${searchText}))".toRegex()).forEach {
            if (it == searchText)
                builder.append(
                    AnnotatedString(
                        it,
                        spanStyle = SpanStyle(background = Color.Yellow),
                    )
                )
            else
                builder.append(
                    AnnotatedString(
                        it,
                        spanStyle = SpanStyle(color),
                    )
                )

        }
    } catch (e: PatternSyntaxException) {
        //TODO:
        return composeLog
    }
    return builder.toAnnotatedString()
}

@Composable
internal fun LogBox(modifier: Modifier = Modifier, logs: List<Log>, searchText: String = "") {
    val lazyListState = rememberLazyListState()

    Box(modifier) {
        LazyColumn(state = lazyListState, modifier = Modifier.animateContentSize()) {
            items(logs) { adaptiveLog ->
                SelectionContainer {
                    Text(adaptiveLog.parseInSearch(searchText))
                }
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = lazyListState
            )
        )
    }

    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty())
            lazyListState.animateScrollToItem(logs.size)
    }
}

@Composable
internal fun CommandSendBox(logger: MiraiLogger, modifier: Modifier = Modifier) {
    var currentCommand by remember(logger) { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val onClick: () -> Unit = {
        scope.launch {
            SolveCommandResult(currentCommand, logger)
            currentCommand = ""
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
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

        Button(
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


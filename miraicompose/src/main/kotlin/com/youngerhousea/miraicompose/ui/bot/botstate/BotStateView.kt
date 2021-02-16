package com.youngerhousea.miraicompose.ui.bot.botstate

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.console.logs
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.theme.AppTheme
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.console.command.descriptor.AbstractCommandValueParameter
import net.mamoe.mirai.console.command.descriptor.CommandReceiverParameter
import net.mamoe.mirai.console.command.descriptor.CommandValueParameter
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.command.parse.CommandCall
import net.mamoe.mirai.console.command.parse.CommandValueArgument
import net.mamoe.mirai.console.util.cast
import net.mamoe.mirai.console.util.safeCast
import net.mamoe.mirai.utils.warning
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

@Composable
fun BotStateView(bot: ComposeBot) = Box(
    Modifier
        .fillMaxSize(),
    contentAlignment = Alignment.BottomCenter
) {
    Column {
        LogBox(
            bot,
            Modifier
                .fillMaxWidth()
                .weight(8f)
                .padding(horizontal = 40.dp, vertical = 20.dp)
        )
        CommandSendBox(
            bot,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 40.dp)
        )
    }
}

@Composable
private fun LogBox(bot: ComposeBot, modifier: Modifier = Modifier) {
    Card(modifier, elevation = 0.dp) {
        LazyColumn(
            Modifier
                .fillMaxWidth()
        ) {
            items(bot.logs) {
                Text(it, modifier = Modifier.padding(vertical = 5.dp))
            }
        }
    }
}

@OptIn(ExperimentalCommandDescriptors::class)
@Composable
private fun CommandSendBox(bot: ComposeBot, modifier: Modifier = Modifier) {
    var currentCommand by remember(bot) { mutableStateOf("") }
    Row(modifier) {
        TextField(
            currentCommand,
            onValueChange = {
                currentCommand = it
            },
            modifier = Modifier
                .weight(13f),
            singleLine = true,
        )

        Spacer(
            Modifier.weight(1f)
        )

        FloatingActionButton(
            {
                MiraiConsole.launch {
                    solveCommandResult(currentCommand, bot)
                }.invokeOnCompletion {
                    currentCommand = ""
                }
            }, modifier = Modifier
                .weight(2f),
            backgroundColor = AppTheme.Colors.backgroundDark
        ) {
            Text("发送")
        }
    }
}


@OptIn(ExperimentalCommandDescriptors::class)
private suspend fun solveCommandResult(
    currentCommand: String,
    bot: ComposeBot
) {
    when (val result = ConsoleCommandSender.executeCommand(currentCommand)) {
        is CommandExecuteResult.Success -> {

        }
        is CommandExecuteResult.IllegalArgument -> {
            val message = result.exception.message
            if (message != null) {
                bot.logger.identity
                bot.logger.warning(message)
            } else bot.logger.warning(result.exception)
        }
        is CommandExecuteResult.ExecutionFailed -> {
            bot.logger.error(result.exception)
        }
        is CommandExecuteResult.UnresolvedCommand -> {
            bot.logger.warning { "未知指令: ${currentCommand}, 输入 ? 获取帮助" }
        }
        is CommandExecuteResult.PermissionDenied -> {
            bot.logger.warning { "权限不足." }
        }
        is CommandExecuteResult.UnmatchedSignature -> {
            bot.logger.warning { "参数不匹配, 你是否想执行: \n" + result.failureReasons.render(result.command, result.call) }
        }
        is CommandExecuteResult.Failure -> {
            bot.logger.warning { result.toString() }
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
package com.youngerhousea.miraicompose.ui.feature.bot.botstate

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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.youngerhousea.miraicompose.console.logs
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.theme.AppTheme
import com.youngerhousea.miraicompose.ui.common.CommandSendBox
import com.youngerhousea.miraicompose.ui.common.LogBox
import com.youngerhousea.miraicompose.ui.common.LogWindow
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
import net.mamoe.mirai.utils.MiraiLogger
import net.mamoe.mirai.utils.warning
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

@Composable
fun BotStateView(bot: ComposeBot) = Box(
    Modifier
        .fillMaxSize(),
    contentAlignment = Alignment.BottomCenter
) {
    LogWindow(bot.logs, bot.logger)
}


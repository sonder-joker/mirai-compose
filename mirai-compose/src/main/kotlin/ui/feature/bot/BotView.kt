package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.pop
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.ui.feature.bot.state.*
import com.youngerhousea.miraicompose.utils.asComponent
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import net.mamoe.mirai.event.events.BotLeaveEvent
import net.mamoe.mirai.utils.LoginSolver
import kotlin.coroutines.resume

class BotState(
    componentContext: ComponentContext,
    val model: ComposeBot
) : ComponentContext by componentContext {

    sealed class BotStatus : Parcelable {
        object NoLogin : BotStatus()
        class SolvePicCaptcha(val bot: Bot, val data: ByteArray, val result: (String?) -> Unit) : BotStatus()
        class SolveSliderCaptcha(val bot: Bot, val url: String, val result: (String?) -> Unit) : BotStatus()
        class SolveUnsafeDeviceLoginVerify(val bot: Bot, val url: String, val result: (String?) -> Unit) : BotStatus()
        class Online(val bot: ComposeBot) : BotStatus()
    }

    private val router = router(
        initialConfiguration = when (model.state) {
            ComposeBot.State.NoLogin -> BotStatus.NoLogin
            ComposeBot.State.Online -> BotStatus.Online(model)
            ComposeBot.State.Loading -> throw Exception("Can't be in loading!")
        },
        key = model.hashCode().toString(),
        handleBackButton = true,
        childFactory = { configuration: BotStatus, componentContext ->
            when (configuration) {
                is BotStatus.NoLogin ->
                    BotNoLogin(componentContext, onClick = ::onClick)
                        .asComponent { BotNoLoginUi(it) }
                is BotStatus.SolvePicCaptcha ->
                    BotSolvePicCaptcha(
                        componentContext,
                        configuration.bot,
                        configuration.data,
                        configuration.result
                    ).asComponent { BotSolvePicCaptchaUi(it) }
                is BotStatus.SolveSliderCaptcha ->
                    BotSolveSliderCaptcha(
                        componentContext,
                        configuration.bot,
                        configuration.url,
                        configuration.result
                    ).asComponent { BotSolveSliderCaptchaUi(it) }
                is BotStatus.SolveUnsafeDeviceLoginVerify ->
                    BotSolveUnsafeDeviceLoginVerify(
                        componentContext,
                        configuration.bot,
                        configuration.url,
                        configuration.result
                    ).asComponent { BotSolveUnsafeDeviceLoginVerifyUi(it) }
                is BotStatus.Online ->
                    BotOnline(componentContext, configuration.bot).asComponent { BotOnlineUi(it) }
            }
        }
    )

    val state get() = router.state

    private fun onClick(account: Long, password: String) {
        MiraiConsole.launch {
            kotlin.runCatching {
                model.login(account, password) {
                    loginSolver = object : LoginSolver() {
                        private fun errorHandler(
                            continuation: CancellableContinuation<String?>,
                            bot: Bot
                        ) = continuation.invokeOnCancellation {
                            if (it != null) {
                                // pass exception?
                                bot.logger.error(it)
                                router.push(BotStatus.NoLogin)
                            }
                        }

                        // 图片验证码
                        override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? =
                            suspendCancellableCoroutine { continuation ->
                                errorHandler(continuation, bot)
                                router.push(BotStatus.SolvePicCaptcha(bot, data) {
                                    continuation.resume(it)
                                })
                            }

                        // 滑动验证码
                        override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? =
                            suspendCancellableCoroutine { continuation ->
                                errorHandler(continuation, bot)
                                router.push(BotStatus.SolveSliderCaptcha(bot, url) {
                                    continuation.resume(it)
                                })
                            }

                        // 不安全设备验证
                        override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? =
                            suspendCancellableCoroutine { continuation ->
                                errorHandler(continuation, bot)
                                router.push(BotStatus.SolveUnsafeDeviceLoginVerify(bot, url) {
                                    continuation.resume(it)
                                })
                            }
                    }
                }
            }.onSuccess {
                router.push(BotStatus.Online(model))
            }.onFailure {
                router.pop()
            }
        }
    }


}


@Composable
fun BotStateUi(botState: BotState) {
    Children(botState.state) { child ->
        child.instance()
    }
}

@Composable
fun TopView(modifier: Modifier) =
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Events",
            color = LocalContentColor.current.copy(alpha = 0.60f),
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }

@OptIn(InternalCoroutinesApi::class)
@Composable
fun EventListView(event: MutableList<BotEvent>) {
    LazyColumn {
        items(event) { botEvent ->
            Text(ParseEventString(botEvent), color = Color.Red)
        }
    }
}

@Composable
fun ParseEventString(botEvent: BotEvent): String {
    return when (botEvent) {
        is BotInvitedJoinGroupRequestEvent -> "BotInvitedJoinGroupRequestEvent"
        is BotLeaveEvent -> "BotLeaveEvent"
        else -> "Unknown Event"
    }
}

@Composable
fun BotItem(
    bot: ComposeBot,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .aspectRatio(2f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.weight(1f))
        Surface(
            modifier = Modifier
                .weight(3f, fill = false),
            shape = CircleShape,
            color = MaterialTheme.colors.surface.copy(alpha = 0.12f)
        ) {
            Image(bot.avatar, null)
        }

        Column(
            Modifier
                .weight(6f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(bot.nick, fontWeight = FontWeight.Bold, maxLines = 1)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(bot.id, style = MaterialTheme.typography.body2)
            }
        }
        Spacer(Modifier.weight(1f))
    }
}

//@Composable
//fun BotListView(
//    model: MutableList<ComposeBot>,
//    modifier: Modifier = Modifier,
//    onAddButtonClick: () -> Unit,
//    onItemClick: (bot: ComposeBot) -> Unit,
//    onItemRemove: (bot: ComposeBot) -> Unit
//) = Box(modifier) {
//    val scrollState = rememberLazyListState()
//    val itemHeight = 100.dp
//
//    LazyColumn(
//        Modifier
//            .fillMaxSize()
//            .withoutWidthConstraints(),
//        state = scrollState
//    ) {
//        items(model) { item ->
//            BotItem(
//                item,
//                Modifier
//                    .requiredHeight(itemHeight),
//                onItemClick = {
//                    onItemClick(item)
//                },
//            )
//        }
//
//        item {
//            Button(
//                onClick = onAddButtonClick,
//                modifier = Modifier
//                    .requiredHeight(itemHeight)
//                    .aspectRatio(2f)
//                    .padding(24.dp),
//            ) {
//                Text("Add a bot")
//            }
//
//        }
//    }
//    VerticalScrollbar(
//        Modifier.align(Alignment.CenterEnd),
//        scrollState,
//        model.size + 1,
//        itemHeight
//    )
//}



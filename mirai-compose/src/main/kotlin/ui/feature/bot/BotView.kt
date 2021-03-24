package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.youngerhousea.miraicompose.utils.VerticalScrollbar
import com.youngerhousea.miraicompose.utils.asComponent
import com.youngerhousea.miraicompose.utils.withoutWidthConstraints
import kotlinx.coroutines.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.utils.LoginSolver
import kotlin.coroutines.resume

class BotState(
    componentContext: ComponentContext,
    val model: ComposeBot
) : ComponentContext by componentContext {

    private val router = router(
        initialConfiguration = when (model.state) {
            ComposeBot.State.NoLogin -> BotStatus.NoLogin
            ComposeBot.State.Online -> BotStatus.Online(model)
            ComposeBot.State.Loading -> throw Exception("Can't be in loading!")
        },
        key = model.hashCode().toString(),
        handleBackButton = true,
        componentFactory = { configuration: BotStatus, componentContext ->
            when (configuration) {
                is BotStatus.NoLogin ->
                    BotNoLogin(componentContext, onClick = ::onClick).asComponent { BotNoLoginUi(it) }
                is BotStatus.Lo ->
                    BotSolvePicCaptchaLoading(
                        componentContext,
                        configuration.bot,
                        configuration.data,
                        configuration.result
                    ).asComponent { BotSolvePicCaptchaLoadingUi(it) }
                is BotStatus.Load ->
                    BotSolveSliderCaptchaLoading(
                        componentContext,
                        configuration.bot,
                        configuration.url,
                        configuration.result
                    ).asComponent { BotSolveSliderCaptchaLoadingUi(it) }
                is BotStatus.Loading ->
                    BotSolveUnsafeDeviceLoginVerify(
                        componentContext,
                        configuration.bot,
                        configuration.url,
                        configuration.result
                    ).asComponent { BotSolveUnsafeDeviceLoginVerifyUi(it) }
                is BotStatus.Online ->
                    BotOnline(componentContext, configuration.bot.toBot()).asComponent { BotOnlineUi(it) }
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

                        override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? =
                            suspendCancellableCoroutine { continuation ->
                                errorHandler(continuation, bot)
                                router.push(BotStatus.Lo(bot, data) {
                                    continuation.resume(it)
                                })
                            }


                        override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? =
                            suspendCancellableCoroutine { continuation ->
                                errorHandler(continuation, bot)
                                router.push(BotStatus.Load(bot, url) {
                                    continuation.resume(it)
                                })
                            }

                        override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? =
                            suspendCancellableCoroutine { continuation ->
                                errorHandler(continuation, bot)
                                router.push(BotStatus.Loading(bot, url) {
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


    sealed class BotStatus : Parcelable {
        object NoLogin : BotStatus()
        class Lo(val bot: Bot, val data: ByteArray, val result: suspend (String?) -> Unit) : BotStatus()
        class Load(val bot: Bot, val url: String, val result: suspend (String?) -> Unit) : BotStatus()
        class Loading(val bot: Bot, val url: String, val result: suspend (String?) -> Unit) : BotStatus()
        class Online(val bot: ComposeBot) : BotStatus()
    }

}

@Composable
fun BotItem(
    bot: ComposeBot,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
    onItemRemove: () -> Unit,
) {
    Row(
        modifier = modifier
            .aspectRatio(2f)
            .clickable(onClick = onItemClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .weight(2f, fill = false)
                .requiredSize(60.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ) {
            Image(bot.avatar, null)
        }

        Column(
            Modifier
                .weight(3f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(bot.nick, fontWeight = FontWeight.Bold)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(bot.id, style = MaterialTheme.typography.body2)
            }
        }

        Column(
            Modifier
                .weight(1f)
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete",
                modifier = Modifier
                    .clickable(onClick = onItemRemove)
            )
        }
    }
}

@Composable
fun TopView(modifier: Modifier) =
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Bots",
            color = LocalContentColor.current.copy(alpha = 0.60f),
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }


@Composable
fun BotListView(
    model: MutableList<ComposeBot>,
    modifier: Modifier = Modifier,
    onAddButtonClick: () -> Unit,
    onItemClick: (bot: ComposeBot) -> Unit,
    onItemRemove: (bot: ComposeBot) -> Unit
) = Box(modifier) {
    val scrollState = rememberLazyListState()
    val itemHeight = 100.dp

    LazyColumn(
        Modifier
            .fillMaxSize()
            .withoutWidthConstraints(),
        state = scrollState
    ) {
        items(model) { item ->
            BotItem(
                item,
                Modifier
                    .requiredHeight(itemHeight),
                onItemClick = {
                    onItemClick(item)
                },
                onItemRemove = {
                    onItemRemove(item)
                }
            )
        }

        item {
            Button(
                onClick = onAddButtonClick,
                modifier = Modifier
                    .requiredHeight(itemHeight)
                    .aspectRatio(2f)
                    .padding(24.dp),
            ) {
                Text("Add a bot")
            }

        }
    }

    VerticalScrollbar(
        Modifier.align(Alignment.CenterEnd),
        scrollState,
        model.size + 1,
        itemHeight
    )
}


@Composable
fun BotStateUi(botState: BotState) {
    Children(botState.state) { child, _ ->
        child()
    }
}

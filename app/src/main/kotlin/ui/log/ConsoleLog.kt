package com.youngerhousea.mirai.compose.ui.log

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import com.youngerhousea.mirai.compose.LocalMiraiCompose
import com.youngerhousea.mirai.compose.console.impl.Log
import com.youngerhousea.mirai.compose.console.viewModel
import com.youngerhousea.mirai.compose.resource.R
import com.youngerhousea.mirai.compose.viewmodel.ConsoleLog
import com.youngerhousea.mirai.compose.viewmodel.ConsoleLogAction
import com.youngerhousea.mirai.compose.viewmodel.ConsoleLogViewModel
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ConsoleLog(consoleLog: ConsoleLog = viewModel { ConsoleLogViewModel() }) {
    val state by consoleLog.state
    val log by LocalMiraiCompose.current.logStorage.collectAsState()

    Scaffold(
        modifier = Modifier.onPreviewCtrlFDown { consoleLog.dispatch(ConsoleLogAction.SetSearchBar) },
        topBar = {
            if (state.searchBarVisible)
                TextField(
                    value = state.searchContent,
                    onValueChange = { consoleLog.dispatch(ConsoleLogAction.UpdateSearchContent(it)) },
                    leadingIcon = { Icon(R.Icon.Search, null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp)
                        .animateContentSize(),
                    shape = RoundedCornerShape(15.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
        }, floatingActionButton = {
//        FloatingActionButton(onClick = {
//
//        }) {
//        }
        }) {
        Column(Modifier.padding(it)) {
            LogBox(
                Modifier
                    .fillMaxSize()
                    .weight(8f)
                    .padding(horizontal = 40.dp, vertical = 20.dp),
                logs = log,
                searchText = state.searchContent,
            )
            CommandSendBox(
                command = state.currentCommand,
                onCommandChange = { commandContent ->
                    consoleLog.dispatch(
                        ConsoleLogAction.UpdateCurrentCommand(
                            commandContent
                        )
                    )
                },
                onClick = { consoleLog.dispatch(ConsoleLogAction.EnterCommand) },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 40.dp),
            )
        }
    }
}

internal fun Modifier.onPreviewCtrlFDown(action: () -> Unit): Modifier = onPreviewKeyEvent {
    if (it.isCtrlFDown) {
        action()
        true
    } else false
}

@OptIn(ExperimentalComposeUiApi::class)
internal val KeyEvent.isCtrlFDown
    get() = (key == Key.CtrlLeft || key == Key.CtrlRight) && type == KeyEventType.KeyDown && type == KeyEventType.KeyDown

@Composable
internal fun LogBox(
    modifier: Modifier = Modifier,
    logs: List<Log>,
    searchText: String,
) {
    val lazyListState = rememberLazyListState()

    val renderLog = remember(logs, searchText) { logs.map { it.annotatedString(searchText) } }

    Box(modifier) {
        LazyColumn(state = lazyListState, modifier = Modifier.animateContentSize()) {
            items(renderLog) { adaptiveLog ->
                SelectionContainer {
                    Text(adaptiveLog)
                }
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = lazyListState)
        )
    }

    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty())
            lazyListState.animateScrollToItem(logs.size)
    }
}


private fun Log.annotatedString(
    searchText: String,
): AnnotatedString {
    val builder = AnnotatedString.Builder()
    if (searchText.isEmpty())
        return with(builder) {
            append(
                AnnotatedString(
                    message,
                    spanStyle = SpanStyle(
                        color = kind.color
                    ),
                )
            )
            toAnnotatedString()
        }
    else
        return with(builder) {
            val tok = StringTokenizer(message, searchText, true)
            while (tok.hasMoreTokens()) {
                val next = tok.nextToken()
                if (next == searchText) {
                    append(
                        AnnotatedString(
                            next,
                            spanStyle = SpanStyle(
                                color = R.Colors.SearchText
                            ),
                        )
                    )
                } else {
                    append(
                        AnnotatedString(
                            next,
                            spanStyle = SpanStyle(
                                kind.color
                            ),
                        )
                    )
                }
            }
            toAnnotatedString()
        }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun CommandSendBox(
    command: String,
    onCommandChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = command,
            onValueChange = onCommandChange,
            singleLine = true,
            modifier = Modifier
                .weight(13f)
                .onPreviewEnterDown(action = onClick)
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

internal fun Modifier.onPreviewEnterDown(action: () -> Unit): Modifier = onPreviewKeyEvent {
    if (it.isEnterDown) {
        action()
        true
    } else false
}

@OptIn(ExperimentalComposeUiApi::class)
internal val KeyEvent.isEnterDown
    get() = key == Key.Enter && type == KeyEventType.KeyDown
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.youngerhousea.miraicompose.app.utils.ColorSerializer
import com.youngerhousea.miraicompose.core.console.Log
import com.youngerhousea.miraicompose.core.console.LogPriority
import com.youngerhousea.miraicompose.core.console.compositionLog
import com.youngerhousea.miraicompose.core.data.LogColor
import net.mamoe.yamlkt.Yaml
import java.util.regex.PatternSyntaxException


@Composable
internal fun LogBox(
    modifier: Modifier = Modifier,
    logs: List<Log>,
    searchText: String,
    logColor: LogColor
) {
    val lazyListState = rememberLazyListState()

    val renderLog by remember(logs, searchText) { derivedStateOf { logs.map { it.annotatedString(searchText, logColor) } } }

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


private fun Log.color(logColor: LogColor): String = when (logPriority) {
    LogPriority.VERBOSE -> logColor.verbose
    LogPriority.INFO -> logColor.info
    LogPriority.WARNING -> logColor.warning
    LogPriority.ERROR -> logColor.error
    LogPriority.DEBUG -> logColor.debug
}

private fun Log.annotatedString(
    searchText: String,
    logColor: LogColor
): AnnotatedString {
    val builder = AnnotatedString.Builder()
    if (searchText == "")
        builder.append(
            AnnotatedString(
                compositionLog,
                spanStyle = SpanStyle(
                    Yaml.decodeFromString(
                        ColorSerializer,
                        color(logColor)
                    )
                ),
            )
        )
    else
        try {
            compositionLog.split("((?<=${searchText})|(?=${searchText}))".toRegex()).forEach {
                if (it == searchText)
                    builder.append(
                        AnnotatedString(
                            it,
                            spanStyle = SpanStyle(
                                background = Yaml.decodeFromString(
                                    ColorSerializer,
                                    logColor.highLight
                                )
                            ),
                        )
                    )
                else
                    builder.append(
                        AnnotatedString(
                            it,
                            spanStyle = SpanStyle(
                                Yaml.decodeFromString(
                                    ColorSerializer,
                                    color(logColor)
                                )
                            ),
                        )
                    )
            }
        } catch (e: PatternSyntaxException) {
            builder.append(
                AnnotatedString(
                    compositionLog,
                    spanStyle = SpanStyle(
                        Yaml.decodeFromString(
                            ColorSerializer,
                            color(logColor)
                        )
                    ),
                )
            )
        }

    return builder.toAnnotatedString()
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
            command,
            onValueChange = onCommandChange,
            modifier = Modifier
                .weight(13f)
                .onPreviewKeyEvent {
                    if (it.key == Key.Enter) {
                        onClick()
                        return@onPreviewKeyEvent true
                    }
                    true
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


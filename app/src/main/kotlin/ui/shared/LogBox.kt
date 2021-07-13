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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.youngerhousea.miraicompose.app.theme.AppTheme
import com.youngerhousea.miraicompose.core.console.Log
import com.youngerhousea.miraicompose.core.console.LogPriority
import com.youngerhousea.miraicompose.core.console.original
import java.util.regex.PatternSyntaxException

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
            adapter = rememberScrollbarAdapter(scrollState = lazyListState)
        )
    }

    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty())
            lazyListState.animateScrollToItem(logs.size)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun CommandSendBox(
    command: String,
    onCommandChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onClick:() -> Unit
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


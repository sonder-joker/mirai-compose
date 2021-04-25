package com.youngerhousea.miraicompose.ui.feature.log

import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.future.inject
import com.youngerhousea.miraicompose.ui.common.CommandSendBox
import com.youngerhousea.miraicompose.ui.common.LogBox
import net.mamoe.mirai.utils.MiraiLogger
import java.awt.event.MouseEvent

/**
 * Compose的所有日志
 *
 */
class ConsoleLog(
    componentContext: ComponentContext,
) : ComponentContext by componentContext {
    val loggerStorage: List<AnnotatedString> by inject()

    val logger:MiraiLogger by inject()
}

@Composable
fun ConsoleLogUi(consoleLog: ConsoleLog) {
    var offset by remember { mutableStateOf(DpOffset(100.dp, 100.dp)) }
    var isExpand by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(top = offset.y - 80.dp)
            .offset(x = offset.x)
    ) {
        DropdownMenu(
            isExpand,
            onDismissRequest = { isExpand = false }
        ) {
            DropdownMenuItem(onClick = { isExpand = false }) {
                Text("OpenLog")
                //TODO open log file in editor such as notepad
            }
            DropdownMenuItem(onClick = { isExpand = false }) {
                Text("Report")
                //TODO report error or do else
            }
        }
    }
    Column {
        LogBox(
            Modifier
                .fillMaxSize()
                .weight(8f)
                .padding(horizontal = 40.dp, vertical = 20.dp)
                .pointerInput(Unit) {
                    onRightClick {
                        isExpand = true
                        offset = DpOffset(it.x.dp, it.y.dp)
                        println(offset)
                    }
                },
            consoleLog.loggerStorage
        )
        CommandSendBox(
            consoleLog.logger,
            Modifier
                .weight(1f)
                .padding(horizontal = 40.dp),
        )
    }
}

private suspend fun PointerInputScope.onRightClick(run: (MouseEvent) -> Unit) {
    suspend fun AwaitPointerEventScope.awaitEventFirstDown(): PointerEvent {
        var event: PointerEvent
        do {
            event = awaitPointerEvent()
        } while (
            !event.changes.all { it.changedToDown() }
        )
        return event
    }

    forEachGesture {
        awaitPointerEventScope {
            awaitEventFirstDown().also { it1 ->
                it1.changes.forEach { it.consumeDownChange() }
            }.mouseEvent?.run {
                if (this.button == MouseEvent.BUTTON3) {
                    run(this)
                }
            }
        }
    }
}

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
import com.youngerhousea.miraicompose.ui.common.CommandSendBox
import com.youngerhousea.miraicompose.ui.common.LogBox
import net.mamoe.mirai.utils.MiraiLogger
import java.awt.event.MouseEvent

suspend fun PointerInputScope.onRightClick(run: (MouseEvent) -> Unit) {
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

class MainLog(
    componentContext: ComponentContext,
    val loggerStorage: List<AnnotatedString>,
    val logger: MiraiLogger
) : ComponentContext by componentContext

@Composable
fun MainLogUi(mainLog: MainLog) {
    var offset by remember { mutableStateOf(DpOffset.Zero) }
    var isExpand by remember { mutableStateOf(false) }
    // 160.dp is the width of nav
    Box(
        modifier = Modifier
            .padding(top = offset.y)
            .offset(x = offset.x - 160.dp)
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
                    }
                },
            mainLog.loggerStorage
        )
        CommandSendBox(
            mainLog.logger,
            Modifier
                .weight(1f)
                .padding(horizontal = 40.dp),
        )
    }
}

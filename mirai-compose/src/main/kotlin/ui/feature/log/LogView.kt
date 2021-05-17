package com.youngerhousea.miraicompose.ui.feature.log

import androidx.compose.desktop.AppManager
import androidx.compose.desktop.AppWindow
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.plus
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.console.ComposeLog
import com.youngerhousea.miraicompose.ui.common.CommandSendBox
import com.youngerhousea.miraicompose.ui.common.LogBox
import net.mamoe.mirai.utils.MiraiLogger
import java.awt.event.MouseEvent
import java.io.File
import java.time.LocalDate
import javax.swing.JFileChooser
import javax.swing.UIManager
import javax.swing.filechooser.FileNameExtensionFilter

/**
 * Compose的所有日志
 *
 */
class ConsoleLog(
    componentContext: ComponentContext,
    val loggerStorage: List<ComposeLog>,
    val logger: MiraiLogger
) : ComponentContext by componentContext

@Composable
fun ConsoleLogUi(consoleLog: ConsoleLog) {
    var offset by remember { mutableStateOf(DpOffset(100.dp, 100.dp)) }
    var isExpand by remember { mutableStateOf(false) }
    var isShowSearch by remember { mutableStateOf(true) }

    val (searchText, setSearchText) = remember { mutableStateOf("") }

    Scaffold(topBar = {
        TopAppBar {
            TextField(
                searchText,
                setSearchText,
                leadingIcon = { Icon(Icons.Default.Search, null) }
            )
        }
    }) {
        Box(
            modifier = Modifier
                .padding(top = offset.y)
                .offset(x = offset.x - 160.dp)
        ) {
            DropdownMenu(
                isExpand,
                onDismissRequest = { isExpand = false }
            ) {
                DropdownMenuItem(onClick = {
                    isExpand = false
                    // open maybe slow
                    val previousLF = UIManager.getLookAndFeel();
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    val fc = JFileChooser()
                    UIManager.setLookAndFeel(previousLF);
                    fc.selectedFile = File("${LocalDate.now()}.log")
                    fc.dialogTitle = "Save log"
                    fc.dragEnabled = true
                    fc.fileSelectionMode = JFileChooser.FILES_ONLY
                    fc.fileFilter = FileNameExtensionFilter("log(*.log, *.txt)", "log", "txt")
                    if (fc.showSaveDialog(AppManager.focusedWindow!!.window) == JFileChooser.APPROVE_OPTION) {
                        val f = fc.selectedFile
                        consoleLog.logger.info("储存当前日志到文件: ${f.absolutePath}")
                        if (f.exists()) {
                            consoleLog.logger.error("储存失败,文件已存在")
                        } else {
                            f.createNewFile()
                            f.writeText(consoleLog.loggerStorage.joinToString("\n"))
                            consoleLog.logger.info("写入完成")
                        }
                    }
                }) {
                    Text("Save log")
                }
//                DropdownMenuItem(onClick = {
//                    //TODO report error or do else
//                    isExpand = false
//                }) {
//                    Text("Report")
//                }
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
                consoleLog.loggerStorage,
                searchText
            )

            CommandSendBox(
                consoleLog.logger,
                Modifier
                    .weight(1f)
                    .padding(horizontal = 40.dp),
            )
        }
    }

    DisposableEffect(Unit) {
        AppManager.windows.first().let {
            (it as AppWindow).keyboard.setShortcut(Key.CtrlLeft + Key.F) {
                isShowSearch = !isShowSearch
            }
        }
        onDispose {
            AppManager.windows.first().let {
                (it as AppWindow).keyboard.removeShortcut(Key.CtrlLeft + Key.F)
            }
        }
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

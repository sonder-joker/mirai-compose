package com.youngerhousea.miraicompose.app.utils

import androidx.compose.desktop.AppManager
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.yamlkt.Yaml
import org.jetbrains.skija.Image
import java.awt.Desktop
import java.io.File
import java.util.*
import javax.swing.JFileChooser
import javax.swing.UIManager
import javax.swing.filechooser.FileFilter

infix fun ClosedRange<Float>.step(step: Float): Iterable<Float> {
    require(start.isFinite())
    require(endInclusive.isFinite())
    require(step > 0.0) { "Step must be positive, was: $step." }
    val sequence = generateSequence(start) { previous ->
        if (previous == Float.POSITIVE_INFINITY) return@generateSequence null
        val next = previous + step
        if (next > endInclusive) null else next
    }
    return sequence.asIterable()
}
//interface MenuScope : ColumnScope {
//    var isExpand: Boolean
//}
//
//class MenuImpl(columnScope: ColumnScope) : MenuScope, ColumnScope by columnScope {
//    override var isExpand: Boolean by mutableStateOf(false)
//}
//
//@Composable
//fun ScopeDropdownMenu(
//    expanded: Boolean,
//    onDismissRequest: () -> Unit,
//    modifier: Modifier = Modifier,
//    offset: DpOffset = DpOffset(0.dp, 0.dp),
//    content: MenuScope.() -> Unit
//) = DropdownMenu(expanded, onDismissRequest = onDismissRequest, modifier, offset) {
//        content(MenuImpl(this))
//    }
//
//
//
//@Composable
//fun MenuScope.AutoCloseDropDownMenuItem(
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier,
//    enabled: Boolean = true,
//    contentPadding: PaddingValues = MenuDefaults.DropdownMenuItemContentPadding,
//    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
//    content: @Composable RowScope.() -> Unit
//) = DropdownMenuItem(onClick = {
//    onClick()
//    isExpand = false
//}, modifier, enabled, contentPadding, interactionSource, content)



@Composable
inline fun <reified T : Enum<T>> EnumTabRowWithContent(
    enum: T,
    rowModifier: Modifier = Modifier,
    crossinline onClick: (enumValue: T) -> Unit,
    crossinline tabContent: @Composable ColumnScope.(enumValue: T) -> Unit
) {
    TabRow(enum.ordinal, modifier = rowModifier) {
        for (current in enumValues<T>()) {
            Tab(enum == current, onClick = {
                onClick(current)
            }, content = {
                tabContent(current)
            })
        }
    }
}


fun <T> derivedStateOfValue(calculation: () -> T): T {
    val value by derivedStateOf(calculation)
    return value
}

internal fun SkiaImageDecode(byteArray: ByteArray): ImageBitmap =
    Image.makeFromEncoded(byteArray).asImageBitmap()

internal fun Base64ImageDecode(data: String): ImageBitmap =
    SkiaImageDecode(Base64.getDecoder().decode(data.split(",").last()))

fun FileChooser(
    title: String,
    f: FileFilter,
    defaultFile: File = File(""),
    defaultPath: String = ".",
    save: Boolean = true
): JFileChooser? {
    val previousLF = UIManager.getLookAndFeel()
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    val fc = JFileChooser(defaultPath)
    UIManager.setLookAndFeel(previousLF)
    fc.selectedFile = defaultFile
    fc.dialogTitle = title
    fc.dragEnabled = true
    fc.fileSelectionMode = JFileChooser.FILES_ONLY
    fc.fileFilter = f
    if (save) {
        if (fc.showSaveDialog(AppManager.focusedWindow!!.window) == JFileChooser.APPROVE_OPTION) {
            return fc
        }
    } else {
        if (fc.showOpenDialog(AppManager.focusedWindow!!.window) == JFileChooser.APPROVE_OPTION) {
            return fc
        }
    }
    return null
}

inline val Desktop: Desktop get() = java.awt.Desktop.getDesktop()
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
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.yamlkt.Yaml
import org.jetbrains.skija.Image
import java.awt.Desktop
import java.io.File
import java.util.*
import javax.swing.JFileChooser
import javax.swing.UIManager
import javax.swing.filechooser.FileFilter


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
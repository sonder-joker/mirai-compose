package com.youngerhousea.miraicompose.app.utils

import androidx.compose.desktop.AppManager
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.Slider
import androidx.compose.material.SliderColors
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import org.jetbrains.skija.Image
import java.awt.Desktop
import java.io.File
import java.util.*
import javax.swing.JFileChooser
import javax.swing.UIManager
import javax.swing.filechooser.FileFilter

@Composable
fun IntSlider(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: IntRange = 0..1,
    /*@IntRange(from = 0)*/
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: SliderColors = SliderDefaults.colors()
) = Slider(
    value = value.toFloat(),
    onValueChange = { onValueChange(it.toInt()) },
    modifier,
    enabled,
    valueRange = valueRange.first.toFloat()..valueRange.last.toFloat(),
    steps,
    onValueChangeFinished,
    interactionSource,
    colors
)



internal fun SkiaImageDecode(byteArray: ByteArray): ImageBitmap =
    Image.makeFromEncoded(byteArray).asImageBitmap()

internal fun Base64ImageDecode(data: String): ImageBitmap =
    SkiaImageDecode(Base64.getDecoder().decode(data.split(",").last()))


@Composable
inline fun <T> ColumnScope.items(
    items: List<T>,
    crossinline itemContent: @Composable ColumnScope.(item: T) -> Unit
) {
    for (item in items) {
        itemContent(item)
    }
}


@Composable
inline fun <T> ColumnScope.itemsWithIndexed(
    items: List<T>,
    crossinline itemContent: @Composable ColumnScope.(item: T, index: Int) -> Unit
) {
    for ((index, item) in items.withIndex()) {
        itemContent(item, index)
    }
}



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

inline val Desktop: Desktop get()  = java.awt.Desktop.getDesktop()
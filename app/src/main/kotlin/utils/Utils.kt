package com.youngerhousea.miraicompose.utils

import androidx.compose.desktop.AppManager
import androidx.compose.desktop.LocalAppWindow
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Slider
import androidx.compose.material.SliderColors
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.Navigator
import com.arkivanov.decompose.instancekeeper.InstanceKeeper
import com.arkivanov.decompose.instancekeeper.getOrCreate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.jetbrains.skija.Image
import java.awt.Cursor
import java.awt.Desktop
import java.io.File
import java.net.URL
import java.net.URLDecoder
import java.util.*
import javax.swing.JFileChooser
import javax.swing.UIManager
import javax.swing.filechooser.FileFilter
import kotlin.collections.set

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


fun URL.splitQuery(): Map<String, String> {
    val queryPairs: MutableMap<String, String> = LinkedHashMap()
    val query: String = this.query
    val pairs = query.split("&".toRegex()).toTypedArray()
    for (pair in pairs) {
        val idx = pair.indexOf("=")
        queryPairs[URLDecoder.decode(pair.substring(0, idx), "UTF-8")] =
            URLDecoder.decode(pair.substring(idx + 1), "UTF-8")
    }
    return queryPairs
}

internal fun SkiaImageDecode(byteArray: ByteArray): ImageBitmap =
    Image.makeFromEncoded(byteArray).asImageBitmap()

internal fun Base64ImageDecode(data: String): ImageBitmap =
    SkiaImageDecode(Base64.getDecoder().decode(data.split(",").last()))


fun <C : Any> Navigator<C>.pushIfNotCurrent(configuration: C) {
    navigate { if (it.last() != configuration) it + configuration else it }
}

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

class ComponentScope(private val scope: CoroutineScope = MainScope()) :
    InstanceKeeper.Instance,
    CoroutineScope by scope {
    override fun onDestroy() {
        scope.cancel()
    }
}

fun ComponentContext.componentScope() = instanceKeeper.getOrCreate(::ComponentScope)

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
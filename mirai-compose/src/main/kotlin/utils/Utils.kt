package com.youngerhousea.miraicompose.utils

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Slider
import androidx.compose.material.SliderColors
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import com.arkivanov.decompose.Navigator
import com.arkivanov.decompose.instancekeeper.InstanceKeeper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.util.CoroutineScopeUtils.childScope
import org.jetbrains.skija.Image
import java.net.URL
import java.net.URLDecoder
import java.util.*
import kotlin.collections.set

//https://stackoverflow.com/questions/44057578/hex-to-rgb-converter-android-studio
fun getARGB(rgb: String): IntArray {
    return when (rgb.length) {
        6 -> {
            val r = Integer.parseInt(rgb.substring(0, 2), 16) // 16 for hex
            val g = Integer.parseInt(rgb.substring(2, 4), 16) // 16 for hex
            val b = Integer.parseInt(rgb.substring(4, 6), 16) // 16 for hex
            intArrayOf(r, g, b)
        }
        8 -> {
            val a = Integer.parseInt(rgb.substring(0, 2), 16) // 16 for hex
            val r = Integer.parseInt(rgb.substring(2, 4), 16) // 16 for hex
            val g = Integer.parseInt(rgb.substring(4, 6), 16) // 16 for hex
            val b = Integer.parseInt(rgb.substring(6, 8), 16) // 16 for hex
            intArrayOf(a, r, g, b)
        }
        else -> {
            throw InputMismatchException()
        }
    }
}

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

@Composable
internal fun VerticalScrollbar(
    modifier: Modifier,
    scrollState: LazyListState,
    itemCount: Int,
    averageItemSize: Dp
) = androidx.compose.foundation.VerticalScrollbar(
    rememberScrollbarAdapter(scrollState, itemCount, averageItemSize),
    modifier
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

internal fun Base64ImageDecode(data: String): ImageBitmap {
    val base64Image = data.split(",").last()
    return SkiaImageDecode(Base64.getDecoder().decode(base64Image))
}

fun Modifier.withoutWidthConstraints() = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints.copy(maxWidth = Int.MAX_VALUE))
    layout(constraints.maxWidth, placeable.height) {
        placeable.place(0, 0)
    }
}

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

internal fun AnnotatedString.chunked(size: Int): List<AnnotatedString> {
    return windowed(size, size, partialWindows = true)
}

internal fun AnnotatedString.windowed(
    size: Int,
    step: Int = 1,
    partialWindows: Boolean = false
): List<AnnotatedString> {
    return windowed(size, step, partialWindows) { it }
}

internal fun <R> AnnotatedString.windowed(
    size: Int,
    step: Int = 1,
    partialWindows: Boolean = false,
    transform: (AnnotatedString) -> R
): List<R> {
    checkWindowSizeStep(size, step)
    val thisSize = this.length
    val resultCapacity = thisSize / step + if (thisSize % step == 0) 0 else 1
    val result = ArrayList<R>(resultCapacity)
    var index = 0
    while (index in 0 until thisSize) {
        val end = index + size
        val coercedEnd = if (end < 0 || end > thisSize) {
            if (partialWindows) thisSize else break
        } else end
        result.add(transform(subSequence(index, coercedEnd)))
        index += step
    }
    return result
}

private fun checkWindowSizeStep(size: Int, step: Int) {
    require(size > 0 && step > 0) {
        if (size != step)
            "Both size $size and step $step must be greater than zero."
        else
            "size $size must be greater than zero."
    }
}

class ComponentChildScope(private val scope: CoroutineScope = MiraiConsole.childScope()) : InstanceKeeper.Instance,
    CoroutineScope by scope {
    override fun onDestroy() {
        scope.cancel()
    }
}
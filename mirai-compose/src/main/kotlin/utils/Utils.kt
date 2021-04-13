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
import com.youngerhousea.miraicompose.console.BufferedOutputStream
import net.mamoe.mirai.utils.MiraiLogger
import org.jetbrains.skija.Image
import java.io.PrintStream
import java.util.*

//https://stackoverflow.com/questions/44057578/hex-to-rgb-converter-android-studio
fun getARGB(rgb: String): IntArray {
    if (rgb.length == 6) {
        val r = Integer.parseInt(rgb.substring(0, 2), 16) // 16 for hex
        val g = Integer.parseInt(rgb.substring(2, 4), 16) // 16 for hex
        val b = Integer.parseInt(rgb.substring(4, 6), 16) // 16 for hex
        return intArrayOf(r, g, b)
    } else if (rgb.length == 8) {
        val a = Integer.parseInt(rgb.substring(0, 2), 16) // 16 for hex
        val r = Integer.parseInt(rgb.substring(2, 4), 16) // 16 for hex
        val g = Integer.parseInt(rgb.substring(4, 6), 16) // 16 for hex
        val b = Integer.parseInt(rgb.substring(6, 8), 16) // 16 for hex
        return intArrayOf(a, r, g, b)
    } else {
        throw InputMismatchException()
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


@Suppress("NOTHING_TO_INLINE")
internal inline fun SkiaImageDecode(byteArray: ByteArray): ImageBitmap =
    Image.makeFromEncoded(byteArray).asImageBitmap()

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


internal fun setSystemOut(out: MiraiLogger) {
    System.setOut(
        PrintStream(
            BufferedOutputStream(
                logger = out.run { ({ line: String? -> info(line) }) }
            ),
            false,
            "UTF-8"
        )
    )
    System.setErr(
        PrintStream(
            BufferedOutputStream(
                logger = out.run { ({ line: String? -> warning(line) }) }
            ),
            false,
            "UTF-8"
        )
    )
}

internal fun <T, K> CrossFade(): @Composable (currentChild: T, currentKey: K, children: @Composable (T, K) -> Unit) -> Unit =
    { currentChild: T, currentKey: K, children: @Composable (T, K) -> Unit ->
        KeyedCrossFade(currentChild, currentKey, children)
    }


@Composable
private fun <T, K> KeyedCrossFade(currentChild: T, currentKey: K, children: @Composable (T, K) -> Unit) {
    androidx.compose.animation.Crossfade(ChildWrapper(currentChild, currentKey)) {
        children(it.child, it.key)
    }
}


internal class ChildWrapper<out T, out C>(val child: T, val key: C) {
    override fun equals(other: Any?): Boolean = key == (other as? ChildWrapper<*, *>)?.key
    override fun hashCode(): Int = key.hashCode()
}

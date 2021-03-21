package org.jetbrains.compose.desktop.browser

import androidx.compose.desktop.AppManager
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import org.jetbrains.skija.Bitmap
import org.jetbrains.skija.IRect
import org.jetbrains.skiko.HardwareLayer
import java.awt.Component
import java.awt.Point
import java.awt.event.*
import javax.swing.JFrame

class BrowserSlicer(val size: IntSize) : Browser {
    private lateinit var bitmap: MutableState<Bitmap>
    private lateinit var recomposer: MutableState<Any>
    private var browser: CefBrowserWrapper? = null
    private val isReady = mutableStateOf(false)
    fun isReady(): Boolean {
        return isReady.value
    }

    private var slices = mutableListOf<BrowserSlice>()
    private var tail: BrowserSlice? = null
    private var entire: BrowserSlice? = null

    @Composable
    fun full() {
        if (isReady()) {
            invalidate()

            entire = remember { BrowserSlice(this, 0, size.height) }
            entire!!.view(bitmap.value, recomposer)
        }
    }

    @Composable
    fun slice(offset: Int, height: Int) {
        if (isReady()) {
            invalidate()

            val slice = BrowserSlice(this, offset, height)
            slices.add(slice)
            slice.view(bitmap.value, recomposer)
        }
    }

    @Composable
    fun tail() {
        if (isReady()) {
            invalidate()

            var offset = 0
            for (slice in slices) {
                val bottom = slice.offset + slice.height
                if (offset < bottom) {
                    offset = bottom
                }
            }

            tail = remember { BrowserSlice(this, offset, size.height - offset) }
            tail!!.view(bitmap.value, recomposer)
        }
    }

    fun updateSize(size: IntSize) {
        browser?.onLayout(0, 0, size.width, size.height)
    }

    override fun load(url: String) {
        if (browser == null) {
            val frame = AppManager.focusedWindow
            if (frame != null) {
                val window = frame.window
                if (!window.isVisible()) {
                    return
                }
                var layer = getHardwareLayer(window)
                if (layer == null) {
                    throw Error("Browser initialization failed!")
                }
                browser = CefBrowserWrapper(
                    startURL = url,
                    layer = layer
                )
                browser?.onActive()
                updateSize(size)
                addListeners(layer)
                isReady.value = true
            }
            return
        }
        browser?.loadURL(url)
        isReady.value = true
    }

    fun dismiss() {
        browser?.onDismiss()
    }

    private fun getHardwareLayer(window: JFrame): HardwareLayer? {
        val components = window.getContentPane().getComponents()
        for (component in components) {
            if (component is HardwareLayer) {
                return component
            }
        }
        return null
    }

    private fun addListeners(layer: Component) {
        layer.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(event: MouseEvent) {
                val slice = isInLayer(event)
                if (slice != null) {
                    event.translatePoint(-slice.x, -slice.y + slice.offset)
                    browser?.onMouseEvent(event)
                }
            }

            override fun mouseReleased(event: MouseEvent) {
                val slice = isInLayer(event)
                if (slice != null) {
                    event.translatePoint(-slice.x, -slice.y + slice.offset)
                    browser?.onMouseEvent(event)
                }
            }
        })

        layer.addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseMoved(event: MouseEvent) {
                val slice = isInLayer(event)
                if (slice != null) {
                    event.translatePoint(-slice.x, -slice.y + slice.offset)
                    browser?.onMouseEvent(event)
                }
            }

            override fun mouseDragged(event: MouseEvent) {
                val slice = isInLayer(event)
                if (slice != null) {
                    event.translatePoint(-slice.x, -slice.y + slice.offset)
                    browser?.onMouseEvent(event)
                }
            }
        })

        layer.addMouseWheelListener(object : MouseWheelListener {
            override fun mouseWheelMoved(event: MouseWheelEvent) {
                val slice = isInLayer(event)
                if (slice != null) {
                    event.translatePoint(-slice.x, -slice.y + slice.offset)
                    browser?.onMouseScrollEvent(event)
                }
            }
        })

        layer.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(event: KeyEvent) {
                browser?.onKeyEvent(event)
            }

            override fun keyReleased(event: KeyEvent) {
                browser?.onKeyEvent(event)
            }

            override fun keyTyped(event: KeyEvent) {
                browser?.onKeyEvent(event)
            }
        })
    }

    private fun isInLayer(event: MouseEvent): BrowserSlice? {
        if (entire != null && isHovered(event.point, entire!!)) {
            return entire
        }
        if (tail != null && isHovered(event.point, tail!!)) {
            return tail
        }
        for (slice in slices) {
            if (isHovered(event.point, slice)) {
                return slice
            }
        }
        return null
    }

    private fun isHovered(point: Point, slice: BrowserSlice): Boolean {
        if (
            point.x >= slice.x &&
            point.x <= slice.x + size.width &&
            point.y >= slice.y &&
            point.y <= slice.y + slice.height
        ) {
            return true
        }
        return false
    }

    internal fun getBitmap(): Bitmap {
        return browser!!.getBitmap()
    }

    private var invalidated = false

    @Composable
    private fun invalidate() {
        if (!invalidated) {
            bitmap = remember { mutableStateOf(emptyBitmap) }
            recomposer = remember { mutableStateOf(Any()) }
            browser!!.onInvalidate = {
                bitmap.value = getBitmap()
                recomposer.value = Any()
            }
            invalidated = true
        }
    }
}

private class BrowserSlice(val handler: BrowserSlicer, val offset: Int, val height: Int) {
    var x: Int = 0
        private set
    var y: Int = 0
        private set

    @OptIn(
        ExperimentalFoundationApi::class
    )
    @Composable
    fun view(bitmap: Bitmap, recomposer: MutableState<Any>) {
        val focusRequester = FocusRequester()

        Box(
            modifier = Modifier.background(color = Color.White)
                .size(handler.size.width.dp, height.dp)
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(handler.size.width, height) {
                        placeable.placeRelative(0, 0)
                    }
                }
                .onGloballyPositioned { coordinates ->
                    x = coordinates.positionInWindow().x.toInt()
                    y = coordinates.positionInWindow().y.toInt()
                }
                .focusRequester(focusRequester)
                .focusable()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { focusRequester.requestFocus() })
        ) {
            Canvas(
                modifier = Modifier.size(handler.size.width.dp, height.dp)
            ) {
                drawIntoCanvas { canvas ->
                    recomposer.value
                    canvas.nativeCanvas.drawBitmapIRect(
                        bitmap,
                        IRect(0, offset, handler.size.width, offset + height),
                        IRect(0, 0, handler.size.width, height).toRect()
                    )
                }
            }
        }
    }
}

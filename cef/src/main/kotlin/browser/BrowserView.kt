package org.jetbrains.compose.desktop.browser

//EXPERIMENTAL FOCUS API
import androidx.compose.desktop.AppManager
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import org.jetbrains.skija.Bitmap
import org.jetbrains.skija.IRect
import org.jetbrains.skiko.HardwareLayer
import java.awt.Component
import java.awt.event.*
import javax.swing.JFrame

class BrowserView : Browser {
    private lateinit var bitmap: MutableState<Bitmap>
    private lateinit var recomposer: MutableState<Any>
    internal var browser: CefBrowserWrapper? = null
    private val isReady = mutableStateOf(false)

    fun isReady(): Boolean {
        return isReady.value
    }

    internal var location = IntOffset.Zero
    internal var size = IntSize.Zero

    private var layout: BrowserLayout? = null

    @Composable
    fun view() {
        if (isReady()) {
            invalidate()

            layout = remember { BrowserLayout(this) }
            layout!!.view(bitmap.value, recomposer)
        }
    }

    private var invalidated = false

    @Composable
    private fun invalidate() {
        if (!invalidated) {
            bitmap = remember { mutableStateOf(emptyBitmap) }
            recomposer = remember { mutableStateOf(Any()) }
            browser!!.onInvalidate = {
                bitmap.value = browser!!.getBitmap()
                recomposer.value = Any()
            }
            invalidated = true
        }
    }

    internal fun updateBounds() {
        browser?.onLayout(location.x, location.y, size.width, size.height)
    }

    override fun load(url: String) {
        if (browser == null) {
            val frame = AppManager.focusedWindow
            if (frame != null) {
                val window = frame.window
                if (!window.isVisible) {
                    return
                }
                var layer = getHardwareLayer(window) ?: throw Error("Browser initialization failed!")
                browser = CefBrowserWrapper(
                    startURL = url,
                    layer = layer
                )
                browser?.onActive()
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
        val components = window.contentPane.components
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
                if (isInLayer(event)) {
                    browser?.onMouseEvent(event)
                }
            }

            override fun mouseReleased(event: MouseEvent) {
                if (isInLayer(event)) {
                    browser?.onMouseEvent(event)
                }
            }
        })

        layer.addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseMoved(event: MouseEvent) {
                if (isInLayer(event)) {
                    browser?.onMouseEvent(event)
                }
            }

            override fun mouseDragged(event: MouseEvent) {
                if (isInLayer(event)) {
                    browser?.onMouseEvent(event)
                }
            }
        })

        layer.addMouseWheelListener { event ->
            if (isInLayer(event)) {
                browser?.onMouseScrollEvent(event)
            }
        }

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

    private fun isInLayer(event: MouseEvent): Boolean {
        if (
            event.x >= location.x &&
            event.x <= location.x + size.width &&
            event.y >= location.y &&
            event.y <= location.y + size.height
        ) {
            return true
        }
        return false
    }
}

private class BrowserLayout(val handler: BrowserView) {
    @OptIn(
        ExperimentalFoundationApi::class
    )
    @Composable
    fun view(bitmap: Bitmap, recomposer: MutableState<Any>) {
        val focusRequester = FocusRequester()

        Box(
            modifier = Modifier.background(color = Color.White)
                .fillMaxSize()
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    handler.size = IntSize(placeable.width, placeable.height)
                    handler.updateBounds()
                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative(0, 0)
                    }
                }
                .onGloballyPositioned { coordinates ->
                    handler.location = IntOffset(
                        coordinates.positionInWindow().x.toInt(),
                        coordinates.positionInWindow().y.toInt()
                    )
                }
                .focusRequester(focusRequester)
                .focusable()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { focusRequester.requestFocus() }
        ) {
            Canvas(
                modifier = Modifier.size(handler.size.width.dp, handler.size.height.dp)
            ) {
                drawIntoCanvas { canvas ->
                    recomposer.value
                    canvas.nativeCanvas.drawBitmapRect(
                        bitmap,
                        IRect(0, 0, handler.size.width, handler.size.height).toRect()
                    )
                }
            }
        }
    }
}

package com.youngerhousea.miraicompose.app.future.splitpane

import androidx.compose.desktop.LocalAppWindow
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.awt.Cursor
import kotlin.math.roundToInt

internal data class MinimalSizes(
    val firstPlaceableMinimalSize: Dp,
    val secondPlaceableMinimalSize: Dp
)

/**
 * Pane that place it parts **vertically** from top to bottom and allows to change items **heights**.
 * The [content] block defines DSL which allow you to configure top ([SplitPaneScope.first]),
 * bottom ([SplitPaneScope.second]).
 *
 * @param modifier the modifier to apply to this layout
 * @param splitPaneState the state object to be used to control or observe the split pane state
 * @param content a block which describes the content. Inside this block you can use methods like
 * [SplitPaneScope.first], [SplitPaneScope.second], to describe parts of split pane.
 */
@ExperimentalSplitPaneApi
@Composable
fun VerticalSplitPane(
    modifier: Modifier = Modifier,
    splitPaneState: SplitPaneState = rememberSplitPaneState(),
    content: SplitPaneScope.() -> Unit
) {
    with(SplitPaneScopeImpl(isHorizontal = false, splitPaneState).apply(content)) {
        if (firstPlaceableContent != null && secondPlaceableContent != null) {
            SplitPane(
                modifier = modifier,
                isHorizontal = false,
                splitPaneState = splitPaneState,
                minimalSizesConfiguration = minimalSizes,
                first = firstPlaceableContent!!,
                second = secondPlaceableContent!!,
                splitter = splitter
            )
        } else {
            firstPlaceableContent?.invoke()
            secondPlaceableContent?.invoke()
        }
    }
}

/**
 * Pane that place it parts **horizontally** from left to right and allows to change items **width**.
 * The [content] block defines DSL which allow you to configure left ([SplitPaneScope.first]),
 * right ([SplitPaneScope.second]) parts of split pane.
 *
 * @param modifier the modifier to apply to this layout
 * @param splitPaneState the state object to be used to control or observe the split pane state
 * @param content a block which describes the content. Inside this block you can use methods like
 * [SplitPaneScope.first], [SplitPaneScope.second], to describe parts of split pane.
 */
@ExperimentalSplitPaneApi
@Composable
fun HorizontalSplitPane(
    modifier: Modifier = Modifier,
    splitPaneState: SplitPaneState = rememberSplitPaneState(),
    content: SplitPaneScope.() -> Unit
) {
    with(SplitPaneScopeImpl(isHorizontal = true, splitPaneState).apply(content)) {
        if (firstPlaceableContent != null && secondPlaceableContent != null) {
            SplitPane(
                modifier = modifier,
                isHorizontal = true,
                splitPaneState = splitPaneState,
                minimalSizesConfiguration = minimalSizes,
                first = firstPlaceableContent!!,
                second = secondPlaceableContent!!,
                splitter = splitter
            )
        } else {
            firstPlaceableContent?.invoke()
            secondPlaceableContent?.invoke()
        }
    }

}

/**
 * Internal implementation of default splitter
 *
 * @param isHorizontal describes is it horizontal or vertical split pane
 * @param splitPaneState the state object to be used to control or observe the split pane state
 */

@OptIn(ExperimentalSplitPaneApi::class)
internal fun defaultSplitter(
    isHorizontal: Boolean,
    splitPaneState: SplitPaneState
): Splitter = Splitter(
    measuredPart = {
        DesktopSplitPaneSeparator(isHorizontal)
    },
    handlePart = {
        DesktopHandle(isHorizontal, splitPaneState)
    }
)

private fun Modifier.cursorForHorizontalResize(
    isHorizontal: Boolean
): Modifier = composed {
    var isHover by remember { mutableStateOf(false) }

    if (isHover) {
        LocalAppWindow.current.window.cursor = Cursor(
            if (isHorizontal) Cursor.E_RESIZE_CURSOR else Cursor.S_RESIZE_CURSOR
        )
    } else {
        LocalAppWindow.current.window.cursor = Cursor.getDefaultCursor()
    }
    pointerMoveFilter(
        onEnter = { isHover = true; true },
        onExit = { isHover = false; true }
    )
}

@Composable
private fun DesktopSplitPaneSeparator(
    isHorizontal: Boolean,
    color: Color = MaterialTheme.colors.background
) = Box(
    Modifier
        .run {
            if (isHorizontal) {
                this.width(1.dp)
                    .fillMaxHeight()
            } else {
                this.height(1.dp)
                    .fillMaxWidth()
            }
        }
        .background(color)
)

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
private fun DesktopHandle(
    isHorizontal: Boolean,
    splitPaneState: SplitPaneState
) = Box(
    Modifier
        .pointerInput(splitPaneState) {
            detectDragGestures { change, _ ->
                change.consumeAllChanges()
                splitPaneState.dispatchRawMovement(
                    if (isHorizontal) change.position.x else change.position.y
                )
            }
        }
        .cursorForHorizontalResize(isHorizontal)
        .run {
            if (isHorizontal) {
                this.width(8.dp)
                    .fillMaxHeight()
            } else {
                this.height(8.dp)
                    .fillMaxWidth()
            }
        }
)

/**
 * Internal implementation of split pane that used in all public composable functions
 *
 * @param modifier the modifier to apply to this layout
 * @param isHorizontal describes is it horizontal of vertical split pane
 * @param splitPaneState the state object to be used to control or observe the split pane state
 * @param minimalSizesConfiguration data class ([MinimalSizes]) that provides minimal size for split pane parts
 * @param first first part of split pane, left or top according to [isHorizontal]
 * @param second second part of split pane, right or bottom according to [isHorizontal]
 * @param splitter separator composable, by default [Splitter] is used
 * */

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
internal fun SplitPane(
    modifier: Modifier,
    isHorizontal: Boolean,
    splitPaneState: SplitPaneState,
    minimalSizesConfiguration: MinimalSizes,
    first: @Composable () -> Unit,
    second: @Composable () -> Unit,
    splitter: Splitter
) {
    Layout(
        {
            first()
            splitter.measuredPart()
            second()
            splitter.handlePart()
        },
        modifier,
    ) { measurables, constraints ->
        with(minimalSizesConfiguration) {
            with(splitPaneState) {

                val constrainedMin = constraints.minByDirection(isHorizontal) + firstPlaceableMinimalSize.value

                val constrainedMax =
                    (constraints.maxByDirection(isHorizontal).toFloat() - secondPlaceableMinimalSize.value).let {
                        if (it <= 0 || it <= constrainedMin) {
                            constraints.maxByDirection(isHorizontal).toFloat()
                        } else {
                            it
                        }
                    }

                if (minPosition != constrainedMin) {
                    maxPosition = constrainedMin
                }

                if (maxPosition != constrainedMax) {
                    maxPosition =
                        if ((firstPlaceableMinimalSize + secondPlaceableMinimalSize).value < constraints.maxByDirection(
                                isHorizontal
                            )
                        ) {
                            constrainedMax
                        } else {
                            minPosition
                        }
                }

                val constrainedPosition =
                    (constraints.maxByDirection(isHorizontal) - (firstPlaceableMinimalSize + secondPlaceableMinimalSize).value).let {
                        if (it > 0f) {
                            (it * positionPercentage).coerceIn(constrainedMin, constrainedMax).roundToInt()
                        } else {
                            constrainedMin.roundToInt()
                        }
                    }


                val firstPlaceable = measurables[0].measure(
                    if (isHorizontal) {
                        constraints.copy(
                            minWidth = 0,
                            maxWidth = constrainedPosition
                        )
                    } else {
                        constraints.copy(
                            minHeight = 0,
                            maxHeight = constrainedPosition
                        )
                    }
                )

                val splitterPlaceable = measurables[1].measure(constraints)
                val splitterSize = splitterPlaceable.valueByDirection(isHorizontal)
                val secondPlaceablePosition = constrainedPosition + splitterSize

                val secondPlaceableSize =
                    (constraints.maxByDirection(isHorizontal) - secondPlaceablePosition).coerceIn(
                        0,
                        if (secondPlaceablePosition < constraints.maxByDirection(isHorizontal)) {
                            constraints.maxByDirection(isHorizontal) - secondPlaceablePosition
                        } else {
                            constraints.maxByDirection(isHorizontal)
                        }
                    )

                val secondPlaceable = measurables[2].measure(
                    if (isHorizontal) {
                        constraints.copy(
                            minWidth = 0,
                            maxWidth = secondPlaceableSize
                        )
                    } else {
                        constraints.copy(
                            minHeight = 0,
                            maxHeight = secondPlaceableSize
                        )
                    }
                )

                val handlePlaceable = measurables[3].measure(constraints)
                val handleSize = handlePlaceable.valueByDirection(isHorizontal)
                // TODO support RTL
                val handlePosition = when (splitter.alignment) {
                    SplitterHandleAlignment.BEFORE -> constrainedPosition + splitterSize - handleSize
                    SplitterHandleAlignment.ABOVE -> constrainedPosition + (splitterSize - handleSize) / 2
                    SplitterHandleAlignment.AFTER -> constrainedPosition
                }

                layout(constraints.maxWidth, constraints.maxHeight) {
                    firstPlaceable.place(0, 0)
                    if (isHorizontal) {
                        secondPlaceable.place(secondPlaceablePosition, 0)
                        splitterPlaceable.place(constrainedPosition, 0)
                        if (moveEnabled) {
                            handlePlaceable.place(handlePosition, 0)
                        }
                    } else {
                        secondPlaceable.place(0, secondPlaceablePosition)
                        splitterPlaceable.place(0, constrainedPosition)
                        if (moveEnabled) {
                            handlePlaceable.place(0, handlePosition)
                        }
                    }
                }
            }
        }
    }
}

private fun Constraints.maxByDirection(isHorizontal: Boolean): Int = if (isHorizontal) maxWidth else maxHeight
private fun Constraints.minByDirection(isHorizontal: Boolean): Int = if (isHorizontal) minWidth else minHeight
private fun Placeable.valueByDirection(isHorizontal: Boolean): Int = if (isHorizontal) width else height

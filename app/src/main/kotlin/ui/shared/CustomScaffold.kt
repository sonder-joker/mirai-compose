package com.youngerhousea.miraicompose.app.ui.shared

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout

@Composable
fun Scaffold(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    backgroundColor: Color = MaterialTheme.colors.background,
    contentColor: Color = contentColorFor(backgroundColor),
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier,
        scaffoldState,
        topBar = { },
        bottomBar = {},
        snackbarHost,
        floatingActionButton = {},
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = false,
        drawerContent = null,
        drawerGesturesEnabled = true,
        drawerShape = MaterialTheme.shapes.large,
        drawerElevation = DrawerDefaults.Elevation,
        drawerBackgroundColor = MaterialTheme.colors.surface,
        drawerContentColor = contentColorFor(MaterialTheme.colors.surface),
        drawerScrimColor = DrawerDefaults.scrimColor,
        backgroundColor = backgroundColor,
        contentColor,
        content
    )
}

@Composable
fun CustomScaffold(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    topBar: @Composable () -> Unit = {},
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    backgroundColor: Color = MaterialTheme.colors.background,
    contentColor: Color = contentColorFor(backgroundColor),
    content: @Composable (PaddingValues) -> Unit
) {
    Surface(modifier = modifier, color = backgroundColor, contentColor = contentColor) {
        ScaffoldLayout(
            topBar = topBar,
            content = content,
            snackbar = {
                snackbarHost(scaffoldState.snackbarHostState)
            },
        )
    }
}

@Composable
private fun ScaffoldLayout(
    topBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
    snackbar: @Composable () -> Unit,
) {
    SubcomposeLayout { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight

        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        layout(layoutWidth, layoutHeight) {
            val topBarPlaceables = subcompose(ScaffoldLayoutContent.TopBar, topBar).map {
                it.measure(looseConstraints)
            }

            val topBarHeight = topBarPlaceables.maxByOrNull { it.height }?.height ?: 0

            val snackbarPlaceables = subcompose(ScaffoldLayoutContent.Snackbar, snackbar).map {
                it.measure(looseConstraints)
            }

            val snackbarHeight = snackbarPlaceables.maxByOrNull { it.height }?.height ?: 0


            val snackbarOffsetFromBottom = if (snackbarHeight != 0) {
                snackbarHeight
            } else {
                0
            }

            val bodyContentHeight = layoutHeight - topBarHeight

            val bodyContentPlaceables = subcompose(ScaffoldLayoutContent.MainContent) {
                val innerPadding = PaddingValues()
                content(innerPadding)
            }.map { it.measure(looseConstraints.copy(maxHeight = bodyContentHeight)) }

            // Placing to control drawing order to match default elevation of each placeable

            bodyContentPlaceables.forEach {
                it.place(0, topBarHeight)
            }
            topBarPlaceables.forEach {
                it.place(0, 0)
            }
            snackbarPlaceables.forEach {
                it.place(0, layoutHeight - snackbarOffsetFromBottom)
            }
        }
    }
}

private enum class ScaffoldLayoutContent { TopBar, MainContent, Snackbar }

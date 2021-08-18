package com.youngerhousea.mirai.compose.ui.plugins

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.AwtWindow
import com.youngerhousea.mirai.compose.console.viewModel
import com.youngerhousea.mirai.compose.viewmodel.PluginsRoute
import com.youngerhousea.mirai.compose.viewmodel.PluginsViewModel
import net.mamoe.mirai.console.plugin.PluginManager
import java.awt.FileDialog
import java.awt.Frame

@Composable
fun Plugins(pluginsViewModel: PluginsViewModel = viewModel { PluginsViewModel() }) {
    val state by pluginsViewModel.state

    if(state.isFileChooserVisible) {
        FileDialog {

        }
    }

    when (val route = state.navigate) {
        PluginsRoute.List ->
            PluginList(
                plugins = PluginManager.plugins,
                onPluginClick = { pluginsViewModel.dispatch(PluginsRoute.Single(it)) },
            )
        is PluginsRoute.Single ->
            SinglePlugin(
                plugin = route.plugin,
                onExit = { pluginsViewModel.dispatch(PluginsRoute.List) }
            )

    }
}


@Composable
private fun FileDialog(
    parent: Frame? = null,
    onCloseRequest: (result: String?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(parent, "Choose a file", LOAD) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    onCloseRequest(file)
                }
            }
        }
    },
    dispose = FileDialog::dispose
)

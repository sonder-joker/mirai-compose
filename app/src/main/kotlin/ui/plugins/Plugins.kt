package com.youngerhousea.mirai.compose.ui.plugins

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.AwtWindow
import com.youngerhousea.mirai.compose.console.viewModel
import com.youngerhousea.mirai.compose.viewmodel.Plugins
import com.youngerhousea.mirai.compose.viewmodel.PluginsViewModel
import net.mamoe.mirai.console.plugin.PluginManager
import java.awt.FileDialog
import java.awt.Frame

@Composable
fun Plugins(plugins: Plugins = viewModel { PluginsViewModel() }) {
    val state by plugins.state

    if(state.isFileChooserVisible) {
        FileDialog {

        }
    }

    when (val route = state.navigate) {
        Plugins.Route.List ->
            PluginList(
                plugins = PluginManager.plugins,
                onPluginClick = { plugins.dispatch(Plugins.Route.Single(it)) },
            )
        is Plugins.Route.Single ->
            SinglePlugin(
                plugin = route.plugin,
                onExit = { plugins.dispatch(Plugins.Route.List) }
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

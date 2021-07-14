package com.youngerhousea.miraicompose.app.ui.setting

import androidx.compose.material.Button
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.AwtWindow
import com.youngerhousea.miraicompose.core.component.setting.PluginControlSetting
import java.awt.FileDialog
import java.awt.Frame
import java.nio.file.Path
import kotlin.io.path.Path

@Composable
fun PluginControlSettingUi(pluginControlSetting: PluginControlSetting) {
    val title by pluginControlSetting.currentPath.collectAsState()

    val error by pluginControlSetting.error.collectAsState()

    var isShow by remember { mutableStateOf(false) }

    Button({
        isShow = !isShow
    }) {
        Text("Add Plugin")
    }

    if (isShow)
        ComposeFileDialog {
            if (it != null)
                pluginControlSetting.addPlugin(it)
            isShow = false
        }


    if (error) {
        Snackbar {
            Text("Fuck")
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ComposeFileDialog(
    parent: Frame? = null,
    onCloseRequest: (result: Path?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(parent, "Choose a file", LOAD) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    onCloseRequest(file?.let { Path(it) })
                }
            }
        }
    },
    dispose = FileDialog::dispose
)


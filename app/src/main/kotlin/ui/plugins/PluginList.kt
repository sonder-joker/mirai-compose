package com.youngerhousea.mirai.compose.ui.plugins

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.youngerhousea.mirai.compose.resource.R
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.permission.Permission
import net.mamoe.mirai.console.permission.PermissionId
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.loader.PluginLoader
import java.awt.Desktop
import kotlin.io.path.div

@Composable
fun PluginList(
    plugins: List<Plugin>,
    onPluginClick: (Plugin) -> Unit
) {
    val state = remember { SnackbarHostState() }

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = state),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(50.dp),
                backgroundColor = Color(0xff6EC177),
                contentColor = Color.White,
                onClick = {
                    Desktop.getDesktop().open((MiraiConsole.rootPath / "plugins").toFile())
                },
                shape = MaterialTheme.shapes.medium.copy(CornerSize(percent = 50))
            ) {
                Icon(R.Icon.Add, R.String.Plugin.Add)
            }
        }
    ) {
        LazyVerticalGrid(
            cells = GridCells.Adaptive(300.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp)
        ) {
            items(plugins) { plugin ->
                Card(
                    Modifier
                        .padding(10.dp)
                        .clickable(onClick = { onPluginClick(plugin) })
                        .requiredHeight(150.dp)
                        .fillMaxWidth(),
                    backgroundColor = Color(0xff979595),
                    contentColor = Color(0xffffffff)
                ) {
                    PluginDescription(plugin, Modifier.padding(10.dp))
                }
            }
        }
    }
}

object FakePlugin : Plugin {
    override val isEnabled: Boolean = true
    override val loader: PluginLoader<*, *>
        get() = error("Not yet implemented")
    override val parentPermission: Permission
        get() = error("Not yet implemented")

    override fun permissionId(name: String): PermissionId {
        error("Not yet implemented")
    }

}

// TODO: Need more action to enable preview
@Preview
@Composable
fun PluginPreview() {
    PluginList(listOf(FakePlugin, FakePlugin)) {}
}


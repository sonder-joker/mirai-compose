package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.Router
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.youngerhousea.miraicompose.ui.common.annotatedName
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

/**
 * 选择的插件的页面
 *
 * @see CommonPlugin
 * @see CJvmPlugin
 */
class SpecificPlugin(
    component: ComponentContext,
    val plugin: Plugin,
    val onExitButtonClicked: () -> Unit,
) : ComponentContext by component {
    private val router: Router<Configuration, Component> = router(
        initialConfiguration = when (plugin) {
            is JavaPlugin -> Configuration.Java(plugin)
            is KotlinPlugin -> Configuration.Kotlin(plugin)
            else -> Configuration.Common(plugin)
        },
        handleBackButton = true,
        key = "SpecificPluginRouter",
        childFactory = { configuration, componentContext ->
            when (configuration) {
                is Configuration.Common -> CommonPlugin(
                    componentContext,
                    configuration.plugin
                ).asComponent {
                    CommonPluginUi(it)
                }
                is Configuration.Java -> CJvmPlugin(
                    componentContext,
                    plugin = configuration.javaPlugin,
                ).asComponent {
                    CJvmPluginUi(it)
                }
                is Configuration.Kotlin -> CJvmPlugin(
                    componentContext,
                    plugin = configuration.kotlinPlugin,
                ).asComponent {
                    CJvmPluginUi(it)
                }
            }
        }
    )

    val state get() = router.state

    sealed class Configuration : Parcelable {
        class Common(val plugin: Plugin) : Configuration()
        class Java(val javaPlugin: JavaPlugin) : Configuration()
        class Kotlin(val kotlinPlugin: KotlinPlugin) : Configuration()
    }

}

@Composable
fun SpecificPluginUi(specificPlugin: SpecificPlugin) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                specificPlugin.plugin.annotatedName,
                textAlign = TextAlign.Center,
                maxLines = 1,
            )
        }, navigationIcon = {
            Icon(
                Icons.Default.KeyboardArrowLeft,
                null,
                Modifier.clickable(onClick = specificPlugin.onExitButtonClicked)
            )
        })
    }) {
        Children(specificPlugin.state) { child ->
            child.instance()
        }
    }
}

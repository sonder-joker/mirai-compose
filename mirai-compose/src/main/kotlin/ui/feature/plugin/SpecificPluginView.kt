package com.youngerhousea.miraicompose.ui.feature.plugin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
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
import com.youngerhousea.miraicompose.console.MiraiConsoleRepository
import com.youngerhousea.miraicompose.ui.common.annotatedName
import com.youngerhousea.miraicompose.utils.Component
import com.youngerhousea.miraicompose.utils.asComponent
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

class SpecificPlugin(
    component: ComponentContext,
    val plugin: Plugin,
    val onExitButtonClicked: () -> Unit,
    repository: MiraiConsoleRepository
) : ComponentContext by component {

    sealed class Setting : Parcelable {
        class Common(val plugin: Plugin) : Setting()
        class Java(val javaPlugin: JavaPlugin) : Setting()
        class Kotlin(val kotlinPlugin: KotlinPlugin) : Setting()
    }

    private val router: Router<Setting, Component> = router(
        initialConfiguration = when (plugin) {
            is JavaPlugin -> Setting.Java(plugin)
            is KotlinPlugin -> Setting.Kotlin(plugin)
            else -> Setting.Common(plugin)
        },
        handleBackButton = true,
        key = "SpecificPluginRouter",
        childFactory = { configuration, componentContext ->
            when (configuration) {
                is Setting.Common -> CommonPlugin(componentContext, configuration.plugin).asComponent {
                    CommonPluginUi(it)
                }
                is Setting.Java -> CJvmPlugin(
                    componentContext,
                    plugin = configuration.javaPlugin,
                    accessibleHolder = repository
                ).asComponent {
                    CJvmPluginUi(it)
                }
                is Setting.Kotlin -> CJvmPlugin(
                    componentContext,
                    plugin = configuration.kotlinPlugin,
                    accessibleHolder = repository
                ).asComponent {
                    CJvmPluginUi(it)
                }
            }
        }
    )

    val state get() = router.state
}

@Composable
fun SpecificPluginUi(specificPlugin: SpecificPlugin) {
    Column(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxWidth().requiredHeight(34.dp), contentAlignment = Alignment.CenterStart) {
            Icon(
                Icons.Default.KeyboardArrowLeft,
                null,
                Modifier.clickable(onClick = specificPlugin.onExitButtonClicked)
            )
            Text(
                specificPlugin.plugin.annotatedName,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Children(specificPlugin.state) { child ->
            child.instance()
        }
    }
}

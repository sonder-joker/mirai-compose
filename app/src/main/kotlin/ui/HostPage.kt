package com.youngerhousea.mirai.compose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.youngerhousea.mirai.compose.resource.R
import com.youngerhousea.mirai.compose.ui.about.About
import com.youngerhousea.mirai.compose.ui.message.Message
import com.youngerhousea.mirai.compose.ui.plugins.Plugins
import com.youngerhousea.mirai.compose.ui.setting.Setting
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane

@Composable
fun HostPage() {
    NavHost()
}

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun NavHost(
) {
    var navigate by rememberSaveable { mutableStateOf<HostRoute>(HostRoute.Message) }

    HorizontalSplitPane {
        first {
            NavHostFirst(
                navigate = HostRoute.Message,
                onRouteLogin = { navigate = HostRoute.Message },
                onRoutePlugins = { navigate = HostRoute.Plugins },
                onRouteSetting = { navigate = HostRoute.Setting },
                onRouteAbout = { navigate = HostRoute.About }
            )
        }

        second {
            NavHostSecond(navigate)
        }
    }
}

@Composable
fun NavHostSecond(hostRoute: HostRoute) {
    when (hostRoute) {
        HostRoute.About -> About(R.Version.Frontend, R.Version.Backend)
        HostRoute.Message -> Message()
        HostRoute.Plugins -> Plugins()
        HostRoute.Setting -> Setting()
    }
}

@Composable
fun NavHostFirst(
    navigate: HostRoute,
    onRouteLogin: () -> Unit,
    onRoutePlugins: () -> Unit,
    onRouteSetting: () -> Unit,
    onRouteAbout: () -> Unit,
) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
    ) {
        NavHostFirstBotMenu()
        RailTab(
            onClick = onRouteLogin,
            selected = navigate == HostRoute.Message
        ) {
            Icon(R.Icon.Message, null)
            Text(R.String.RailTabFirst)
        }
        RailTab(
            onClick = onRoutePlugins,
            selected = navigate == HostRoute.Plugins,
        ) {
            Icon(R.Icon.Plugins, null)
            Text(R.String.RailTabSecond)
        }
        RailTab(
            onClick = onRouteSetting,
            selected = navigate == HostRoute.Setting
        ) {
            Icon(R.Icon.Setting, null)
            Text(R.String.RailTabThird)
        }
        RailTab(
            onClick = onRouteAbout,
            selected = navigate == HostRoute.About
        ) {
            Icon(R.Icon.About, null)
            Text(R.String.RailTabFourth)
        }
    }
}


sealed class HostRoute {
    object Message : HostRoute()
    object Setting : HostRoute()
    object About : HostRoute()
    object Plugins : HostRoute()
}
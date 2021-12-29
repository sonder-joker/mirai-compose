package com.youngerhousea.mirai.compose.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youngerhousea.mirai.compose.console.viewModel
import com.youngerhousea.mirai.compose.resource.R
import com.youngerhousea.mirai.compose.ui.about.About
import com.youngerhousea.mirai.compose.ui.log.ConsoleLog
import com.youngerhousea.mirai.compose.ui.message.BotMessage
import com.youngerhousea.mirai.compose.ui.message.Message
import com.youngerhousea.mirai.compose.ui.plugins.Plugins
import com.youngerhousea.mirai.compose.ui.setting.Setting
import com.youngerhousea.mirai.compose.viewmodel.Host
import com.youngerhousea.mirai.compose.viewmodel.HostViewModel
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane

@Composable
fun HostPage() {
    NavHost()
}

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun NavHost(
    hostViewModel: Host = viewModel { HostViewModel() }
) {
    val state by hostViewModel.hostState

    HorizontalSplitPane {
        first(minSize = MinFirstSize) {
            NavHostFirst(
                modifier = Modifier.background(R.Colors.SplitLeft),
                navigate = state.navigate,
                onRouteMessage = { hostViewModel.dispatch(Host.Route.Message) },
                onRoutePlugins = { hostViewModel.dispatch(Host.Route.Plugins) },
                onRouteSetting = { hostViewModel.dispatch(Host.Route.Setting) },
                onRouteAbout = { hostViewModel.dispatch(Host.Route.About) },
                onRouteConsoleLog = { hostViewModel.dispatch(Host.Route.ConsoleLog) }
            )
        }
        second {
            NavHostSecond(state.navigate)
        }
    }
}

@Composable
fun NavHostSecond(hostRoute: Host.Route) {
    when (hostRoute) {
        is Host.Route.About -> About()
        is Host.Route.Message -> Message()
        is Host.Route.Plugins -> Plugins()
        is Host.Route.Setting -> Setting()
        is Host.Route.BotMessage -> BotMessage()
        is Host.Route.ConsoleLog -> ConsoleLog()
    }
}

@Composable
fun NavHostFirst(
    modifier: Modifier = Modifier,
    navigate: Host.Route,
    onRouteMessage: () -> Unit,
    onRoutePlugins: () -> Unit,
    onRouteSetting: () -> Unit,
    onRouteAbout: () -> Unit,
    onRouteConsoleLog: () -> Unit,
) {
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        NavHostFirstBotMenu()
        RailTab(
            onClick = onRouteMessage,
            selected = navigate == Host.Route.Message
        ) {
            Icon(R.Icon.Message, null)
            Text(R.String.RailTabFirst)
        }
        RailTab(
            onClick = onRoutePlugins,
            selected = navigate == Host.Route.Plugins,
        ) {
            Icon(R.Icon.Plugins, null)
            Text(R.String.RailTabSecond)
        }
        RailTab(
            onClick = onRouteSetting,
            selected = navigate == Host.Route.Setting
        ) {
            Icon(R.Icon.Setting, null)
            Text(R.String.RailTabThird)
        }
        RailTab(
            onClick = onRouteAbout,
            selected = navigate == Host.Route.About
        ) {
            Icon(R.Icon.About, null)
            Text(R.String.RailTabFourth)
        }
        RailTab(
            onClick = onRouteConsoleLog,
            selected = navigate == Host.Route.ConsoleLog
        ) {
            Icon(R.Icon.ConsoleLog, null)
            Text(R.String.RailTabFive)
        }
    }
}

private val MinFirstSize = 170.dp

@Preview
@Composable
fun NavHostFirstPreview() {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        RailTab(
            onClick = {},
            selected = true
        ) {
            Icon(R.Icon.Message, null)
            Text(R.String.RailTabFirst)
        }
        RailTab(
            onClick = {},
            selected = false,
        ) {
            Icon(R.Icon.Plugins, null)
            Text(R.String.RailTabSecond)
        }
        RailTab(
            onClick = {},
            selected = false
        ) {
            Icon(R.Icon.Setting, null)
            Text(R.String.RailTabThird)
        }
        RailTab(
            onClick = {},
            selected = false
        ) {
            Icon(R.Icon.About, null)
            Text(R.String.RailTabFourth)
        }
    }
}

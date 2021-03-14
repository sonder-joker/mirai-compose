package com.youngerhousea.miraicompose.ui.feature.log

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.ui.common.CommandSendBox
import com.youngerhousea.miraicompose.ui.common.LogBox
import com.youngerhousea.miraicompose.utils.Component
import net.mamoe.mirai.utils.MiraiInternalApi
import net.mamoe.mirai.utils.MiraiLogger


class Log(
    componentContext: ComponentContext,
    val loggerStorage: List<AnnotatedString>,
    val logger:MiraiLogger
) : Component, ComponentContext by componentContext {
    @MiraiInternalApi
    @Composable
    override fun render() {
        LogWindow(loggerStorage, logger)
    }
}


@Composable
internal fun LogWindow(logs: List<AnnotatedString>, logger: MiraiLogger) {
    Column {
        LogBox(
            logs,
            Modifier
                .weight(8f)
                .padding(horizontal = 40.dp, vertical =  20.dp)
        )
        CommandSendBox(
            logger,
            Modifier
                .weight(1f)
                .padding(horizontal = 40.dp),
        )
    }
}




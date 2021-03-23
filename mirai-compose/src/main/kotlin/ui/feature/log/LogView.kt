package com.youngerhousea.miraicompose.ui.feature.log

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.ui.common.CommandSendBox
import com.youngerhousea.miraicompose.ui.common.LogBox
import net.mamoe.mirai.utils.MiraiLogger


class AllLog(
    componentContext: ComponentContext,
    val loggerStorage: List<AnnotatedString>,
    val logger: MiraiLogger
) : ComponentContext by componentContext {

}


@Composable
fun LogUi(allLog: AllLog) {
    Column {
        LogBox(
            Modifier
                .fillMaxSize()
                .weight(8f)
                .padding(horizontal = 40.dp, vertical = 20.dp),
            allLog.loggerStorage,
        )
        CommandSendBox(
            allLog.logger,
            Modifier
                .weight(1f)
                .padding(horizontal = 40.dp),
        )
    }
}




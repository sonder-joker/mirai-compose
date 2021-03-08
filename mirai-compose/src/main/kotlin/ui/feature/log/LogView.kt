package com.youngerhousea.miraicompose.ui.feature.log

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.ui.common.LogWindow
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


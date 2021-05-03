package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.AnnotatedString
import com.youngerhousea.miraicompose.console.MiraiComposeLogger
import com.youngerhousea.miraicompose.ui.feature.log.ConsoleLog
import com.youngerhousea.miraicompose.ui.feature.log.ConsoleLogUi
import com.youngerhousea.miraicompose.utils.fakeContext
import net.mamoe.mirai.utils.MiraiLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

class ConsoleLogTest : KoinTest {
    val consoleLog = ConsoleLog(fakeContext())

}

package com.youngerhousea.miraicompose

import androidx.compose.ui.test.junit4.createComposeRule
import com.youngerhousea.miraicompose.ui.feature.bot.botstate.BotLoginView
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start
import org.junit.Rule
import org.junit.Test

class BotNoLoginTest{

    @get:Rule
    val rule = createComposeRule()

    val console = MiraiCompose.start()

    @Test
    fun Login() {
        rule.setContent {
            val  account = 0L
            val password = ""
        }
    }
}
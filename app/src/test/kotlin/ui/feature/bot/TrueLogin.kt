package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.ui.test.junit4.createComposeRule
import com.youngerhousea.miraicompose.console.MiraiCompose
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start
import org.junit.Rule

class TrueLogin {
    val compose = MiraiCompose.apply { start() }

//    TODO: login
//    val a = InitLogin(fakeContext(), onClick = { account: Long, password: String ->
//        MiraiConsole.addBot(account, password).login()
//    }).apply { onAccountTextChange(TextFieldValue("123456")) }

    @get:Rule
    val composeTestRule = createComposeRule()


}
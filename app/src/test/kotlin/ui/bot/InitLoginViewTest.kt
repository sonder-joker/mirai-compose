package com.youngerhousea.miraicompose.app.ui.bot

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.youngerhousea.miraicompose.core.component.bot.InitLogin
import net.mamoe.mirai.utils.MiraiInternalApi
import org.junit.Rule
import org.junit.Test

@OptIn(MiraiInternalApi::class)
internal class InitLoginViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val initLogin = object :InitLogin{
        override fun onLogin(account: Long, password: String) {
            println("Success")
        }
    }

    @Test
    fun `ensure has necessary text field`() {
        composeTestRule.setContent {
            InitLoginUi(initLogin)
        }
        composeTestRule.onNodeWithText("Account").assertExists()
        composeTestRule.onNodeWithText("Password").assertExists()
        composeTestRule.onNodeWithText("Login").assertExists()

//        composeTestRule.onNodeWithText("Account").performTextInput("1")
//        composeTestRule.onNodeWithText("Login").performClick()
    }

    @Test
    fun `test number format exception`() {
//        val initLogin = getInitLogin { throw NumberFormatException() }
        composeTestRule.setContent {
            InitLoginUi(initLogin)
        }
//        initLogin.onLogin(account.text.toLong(), password.text)
//        TODO: more useful test after support
//        composeTestRule.onNodeWithText(R.String.numberFormat).assertExists()
    }


    @Test
    fun `test wrong password exception`() {
//        val initLogin = getInitLogin { throw WrongPasswordException("") }
        composeTestRule.setContent {
            InitLoginUi(initLogin)
        }
//        initLogin.onLogin(account.text.toLong(), password.text)
//        composeTestRule.onNodeWithText(R.String.wrongPassword).assertExists()
    }
}
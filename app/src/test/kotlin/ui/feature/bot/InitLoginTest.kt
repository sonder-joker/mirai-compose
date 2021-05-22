package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.text.input.TextFieldValue
import com.youngerhousea.miraicompose.theme.R
import com.youngerhousea.miraicompose.utils.fakeContext
import net.mamoe.mirai.network.WrongPasswordException
import net.mamoe.mirai.utils.MiraiInternalApi
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(MiraiInternalApi::class)
internal class InitLoginTest {
    private fun getInitLogin(onClick: () -> Unit) =
        InitLogin(fakeContext(), onClick = { _, _ ->
            onClick()
        }).apply { onAccountTextChange(TextFieldValue("123456")) }

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `ensure has necessary text field`() {
        val initLogin = getInitLogin {}
        composeTestRule.setContent {
            InitLoginUi(initLogin)
        }
        composeTestRule.onNodeWithText("Account").assertExists()
        composeTestRule.onNodeWithText("Password").assertExists()
        composeTestRule.onNodeWithText("Login").assertExists()
    }

    @Test
    fun `test number format exception`() {
        val initLogin = getInitLogin { throw NumberFormatException() }
        composeTestRule.setContent {
            InitLoginUi(initLogin)
        }
        initLogin.onLogin()
//        TODO: more useful test after support
//        composeTestRule.onNodeWithText(R.String.numberFormat).assertExists()
    }


    @Test
    fun `test wrong password exception`() {
        val initLogin = getInitLogin { throw WrongPasswordException("") }
        composeTestRule.setContent {
            InitLoginUi(initLogin)
        }
        initLogin.onLogin()
//        composeTestRule.onNodeWithText(R.String.wrongPassword).assertExists()
    }
}
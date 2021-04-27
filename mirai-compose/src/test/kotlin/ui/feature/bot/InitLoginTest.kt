package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.youngerhousea.miraicompose.utils.fakeContext
import net.mamoe.mirai.network.WrongPasswordException
import net.mamoe.mirai.utils.MiraiInternalApi
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(MiraiInternalApi::class)
internal class InitLoginTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeContext = fakeContext()


    @Test
    fun ensureHaveText() {
        val initLogin = InitLogin(fakeContext, onClick = { _: Long, _: String -> })
        composeTestRule.setContent {
            InitLoginUi(initLogin)
        }
        composeTestRule.onNodeWithText("Account").assertExists()
        composeTestRule.onNodeWithText("Password").assertExists()
        composeTestRule.onNodeWithText("Login").assertExists()
    }

    @Test
    fun t() {
        val initLogin = InitLogin(fakeContext, onClick = { _, _ ->
            throw WrongPasswordException("")
        })
        initLogin.onLogin()
        assertEquals(initLogin.errorTip, "格式错误")
    }
}
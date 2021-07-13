package com.youngerhousea.miraicompose.core.component.impl.bot

import com.youngerhousea.miraicompose.core.data.LoginCredential
import com.youngerhousea.miraicompose.core.testComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import net.mamoe.mirai.utils.MiraiInternalApi
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(MiraiInternalApi::class, ExperimentalCoroutinesApi::class)
class InitLoginTest {
    private val mainThreadSurrogate = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
    }

    @Test
    fun `test account input`() {
        val initLogin = initLoginImpl {

        }
        initLogin.onAccountChange("123")
        assert(initLogin.model.value.account == "123") { "yes" }
    }

    @Test
    fun `init assert wrong password`() = runBlockingTest {
//        val initLogin = initLoginImpl { account, password ->
//            throw WrongPasswordException("wrong password")
//        }
//        initLogin.onLogin(1234, "pass")
//        delay(2_000)
//        assert(initLogin.model.value.event is Event.Error)
    }

    @Test
    fun `init assert retry later`() {
//        val initLoginImpl = initLoginImpl { account, password ->
//            throw RetryLaterException()
//        }
//        initLoginImpl.onLogin(1234, "pass")
//        assert(initLoginImpl.model.value.event is Event.Error)
    }
//
//    @Test
//    fun `init delay later`() {
//        val initLoginImpl = initLoginImpl { account, password ->
//            delay(30_000)
//        }
//        initLoginImpl.onLogin(1234, "pass")
//        advanceTimeBy(30_100)
//        assert(initLoginImpl.model.value.event is Event.Error)
//    }

}


private fun initLoginImpl(
    onClick: suspend (loginCredential: LoginCredential) -> Unit
) =
    InitLoginImpl(testComponent(), onClick)

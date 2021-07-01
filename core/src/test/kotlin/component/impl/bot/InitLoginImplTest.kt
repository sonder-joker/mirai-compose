package com.youngerhousea.miraicompose.core.component.impl.bot

import com.youngerhousea.miraicompose.core.component.bot.Event
import com.youngerhousea.miraicompose.core.component.bot.InitLogin
import com.youngerhousea.miraicompose.core.testComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runBlockingTest
import net.mamoe.mirai.network.RetryLaterException
import net.mamoe.mirai.network.WrongPasswordException
import net.mamoe.mirai.utils.MiraiInternalApi
import org.junit.Test

@OptIn(MiraiInternalApi::class,ExperimentalCoroutinesApi::class, ObsoleteCoroutinesApi::class)
class InitLoginImplTest {
//    private val mainThreadSurrogate = newSingleThreadContext("test thread")

    @Test
    fun `init assert wrong password`() = runBlockingTest {
        val initLoginImpl = initLoginImpl { account, password ->
            throw WrongPasswordException("wrong password")
        }
        initLoginImpl.onLogin(1234, "pass")
        assert(initLoginImpl.model.value.event is Event.Error)
    }

    @Test
    fun `init assert retry later`() = runBlockingTest {
        val initLoginImpl = initLoginImpl { account, password ->
            throw RetryLaterException()
        }
        initLoginImpl.onLogin(1234, "pass")
        assert(initLoginImpl.model.value.event is Event.Error)
    }

    @Test
    fun `init delay later`()= runBlockingTest {
        val initLoginImpl = initLoginImpl { account, password ->
            delay(30_000)
        }
        initLoginImpl.onLogin(1234, "pass")
        advanceTimeBy(30_100)
        assert(initLoginImpl.model.value.event is Event.Error)
    }

}


private fun initLoginImpl(onClick: suspend (account: Long, password: String) -> Unit): InitLogin =
    InitLoginImpl(testComponent(), onClick)

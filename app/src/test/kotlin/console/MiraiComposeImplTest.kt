package com.youngerhousea.mirai.compose.console

import com.youngerhousea.mirai.compose.console.impl.Lifecycle
import com.youngerhousea.mirai.compose.console.impl.MiraiComposeImpl
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start
import kotlin.test.Test
import kotlin.test.assertEquals

internal class MiraiComposeImplTest {

    @Test
    fun `create viewModel`() {
        val compose: MiraiComposeImplementation = MiraiComposeImpl()
        assertEquals(compose.lifecycle.state, Lifecycle.State.Initialized)
        compose.start()
        assertEquals(compose.lifecycle.state, Lifecycle.State.Live)
        compose.cancel()
        // may need test after cancel can't create viewModel
        assertEquals(compose.lifecycle.state, Lifecycle.State.Destroy)
    }
}


package com.youngerhousea.mirai.compose.console

import com.youngerhousea.mirai.compose.console.impl.ViewModelStoreImpl
import org.junit.Before
import kotlin.test.*


internal class TestViewModel : ViewModel {
    var isDestroy = false

    override fun onDestroy() {
        isDestroy = true
    }
}

internal class ViewModelTest {
    private lateinit var viewModelStore: ViewModelStore

    @Before
    fun init() {
        viewModelStore = ViewModelStoreImpl()
    }

    @Test
    fun `viewModel function test`() {
        assertNull(viewModelStore.get<TestViewModel>(), "Before create ViewModel shouldn't exist")

        val viewModel = viewModelStore.getOrCreate { TestViewModel() }
        assertNotNull(viewModelStore.get<TestViewModel>(), "ViewModel shouldn't be null")

        viewModelStore.clean()
        assertTrue(viewModel.isDestroy, "ViewModel should already destroy")
    }
}
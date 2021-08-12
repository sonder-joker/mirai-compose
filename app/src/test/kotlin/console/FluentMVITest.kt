package com.youngerhousea.mirai.compose.console

import androidx.compose.runtime.mutableStateOf
import org.junit.Test

sealed class TestState {
    object Nothing : TestState()
    class A(
        val string: String
    ) : TestState()

    class B(
        val int: Int
    ) : TestState()

    class C(
        val long: Long
    ) : TestState()
}

sealed class TestEvent {
    object Empty:TestEvent()
    class AToB(val int:Int):TestEvent()
    class BToC(val long: Long):TestEvent()
    class CToA(val string:String):TestEvent()

}

class FluentMVITest {
    val testState = mutableStateOf<TestState>(TestState.Nothing)
    val testEvent = TestEvent.Empty

}


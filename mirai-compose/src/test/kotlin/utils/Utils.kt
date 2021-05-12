package com.youngerhousea.miraicompose.utils

import androidx.compose.ui.text.AnnotatedString
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.lifecycle.LifecycleRegistry
import org.junit.Before

fun fakeContext(): ComponentContext = DefaultComponentContext(LifecycleRegistry())

class UtilsTest{
    val s = mutableListOf<AnnotatedString>()

    @Before
    fun before(){
        s.add(AnnotatedString("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"))
        s.add(AnnotatedString("bbbbbbbbbbbbbbbbbbbbbbb"))
        s.add(AnnotatedString("cccccccccccccccccccccccccccccccccc"))
        s.add(AnnotatedString("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"))
        s.add(AnnotatedString("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"))

    }
}
package com.youngerhousea.miraicompose.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.lifecycle.LifecycleRegistry
import org.junit.Before
import org.junit.Test

fun fakeContext(): ComponentContext = DefaultComponentContext(LifecycleRegistry())

class UtilsTest {
    val s = mutableListOf<AnnotatedString>()

    @Before
    fun before() {
        s.add(AnnotatedString("2021/05/13 20:02:13 I/org.example.my-plugin: Hi: test"))
        s.add(AnnotatedString("bbbbbbbbbbbbbbbbbbbbbbb"))
        s.add(AnnotatedString("cccccccccccccccccccccccccccccccccc"))
        s.add(AnnotatedString("ddddddddddddddddddddddddddddddddddddd"))
        s.add(AnnotatedString("eeeeeeeeeeeeee"))
    }

    @Test
    fun t() {
        for (m in s[0].split(Regex("((?<=a)|(?=a))"))) {
            println(m)
        }
    }
}
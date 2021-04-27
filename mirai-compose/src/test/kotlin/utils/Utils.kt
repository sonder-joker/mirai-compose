package com.youngerhousea.miraicompose.utils

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.lifecycle.LifecycleRegistry

fun fakeContext(): ComponentContext = DefaultComponentContext(LifecycleRegistry())

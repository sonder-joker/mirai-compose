package com.youngerhousea.miraicompose.core

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.lifecycle.LifecycleRegistry


fun testComponent() = DefaultComponentContext(LifecycleRegistry())

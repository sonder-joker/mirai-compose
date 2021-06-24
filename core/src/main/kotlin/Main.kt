package com.youngerhousea.miraicompose.core

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.lifecycle.LifecycleRegistry
import com.youngerhousea.miraicompose.core.component.NavHost
import com.youngerhousea.miraicompose.core.component.impl.NavHostImpl

fun navHost(componentContext: ComponentContext = DefaultComponentContext(LifecycleRegistry())): NavHost = NavHostImpl(componentContext)

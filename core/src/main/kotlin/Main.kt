package com.youngerhousea.miraicompose.core

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.core.component.NavHost
import com.youngerhousea.miraicompose.core.component.impl.NavHostImpl

fun navHost(componentContext: ComponentContext): NavHost = NavHostImpl(componentContext)
package com.youngerhousea.miraicompose.ui.feature.bot

import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.utils.Component

class Choose(
    componentContext: ComponentContext,
    val model: MutableList<ComposeBot>
) : Component, ComponentContext by componentContext {
    override fun render() {
        TODO("Not yet implemented")
    }
}
package com.youngerhousea.miraicompose.core.component.impl.setting

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.instancekeeper.getOrCreate
import com.youngerhousea.miraicompose.core.component.setting.LogLevelSetting
import com.youngerhousea.miraicompose.core.console.LogPriority
import com.youngerhousea.miraicompose.core.data.PriorityNode
import com.youngerhousea.miraicompose.core.viewmodel.LogPriorityViewModel
import kotlinx.coroutines.flow.StateFlow

internal class LogLevelSettingImpl(
    componentContext: ComponentContext,
    private val logPriorityViewModel: LogPriorityViewModel = componentContext.instanceKeeper.getOrCreate { LogPriorityViewModel() }
) : LogLevelSetting, ComponentContext by componentContext {

    override val priorityNode: StateFlow<PriorityNode> get() = logPriorityViewModel.data

    override fun setLogConfigLevel(priority: LogPriority) {

    }
}
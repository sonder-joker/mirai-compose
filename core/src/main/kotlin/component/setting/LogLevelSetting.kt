package com.youngerhousea.miraicompose.core.component.setting

import com.youngerhousea.miraicompose.core.console.LogPriority
import com.youngerhousea.miraicompose.core.data.PriorityNode
import kotlinx.coroutines.flow.StateFlow

interface LogLevelSetting {

    val priorityNode: StateFlow<PriorityNode>

    fun setLogConfigLevel(priority: LogPriority)
    
    fun onExitButtonClicked()
}
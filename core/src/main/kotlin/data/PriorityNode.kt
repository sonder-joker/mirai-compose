package com.youngerhousea.miraicompose.core.data

import com.youngerhousea.miraicompose.core.console.LogPriority
import kotlinx.serialization.Serializable

@Serializable
data class PriorityNode(
    val path: String,
    val logPriority: LogPriority = LogPriority.INFO,
    val priorityNodes: MutableList<PriorityNode> = mutableListOf()
)
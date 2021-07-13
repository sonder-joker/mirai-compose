package com.youngerhousea.miraicompose.core.viewmodel

import com.arkivanov.decompose.instancekeeper.InstanceKeeper
import com.youngerhousea.miraicompose.core.console.LogPriority
import com.youngerhousea.miraicompose.core.loginPriorityPath
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import net.mamoe.yamlkt.Yaml
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText


@Serializable
data class Node(
    val path: String,
    val logPriority: LogPriority = LogPriority.INFO,
    val nodes: MutableList<Node> = mutableListOf()
)

class LogPriorityViewModel(
    val path: Path = loginPriorityPath
) : InstanceKeeper.Instance {

    private val _data = MutableStateFlow(Yaml.decodeFromString(path.readText()) ?: Node("."))

    val data: StateFlow<Node> get() = _data

    override fun onDestroy() {
        path.writeText(Yaml.encodeToString(_data.value))
    }
}

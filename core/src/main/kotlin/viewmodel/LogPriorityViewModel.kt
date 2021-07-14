package com.youngerhousea.miraicompose.core.viewmodel

import com.arkivanov.decompose.instancekeeper.InstanceKeeper
import com.youngerhousea.miraicompose.core.data.PriorityNode
import com.youngerhousea.miraicompose.core.loginPriorityFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.decodeFromString
import net.mamoe.yamlkt.Yaml
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText


class LogPriorityViewModel(
    val path: Path = loginPriorityFile
) : InstanceKeeper.Instance {

    private val _data = MutableStateFlow(Yaml.decodeFromString(path.readText()) ?: PriorityNode("."))

    val data: StateFlow<PriorityNode> get() = _data

    override fun onDestroy() {
        path.writeText(Yaml.encodeToString(_data.value))
    }
}

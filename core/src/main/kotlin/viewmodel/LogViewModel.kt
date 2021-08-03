package com.youngerhousea.miraicompose.core.viewmodel

import com.arkivanov.decompose.instancekeeper.InstanceKeeper
import com.youngerhousea.miraicompose.core.console.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LogViewModel(

) : InstanceKeeper.Instance {
    private val _logs = MutableStateFlow<List<Log>>(listOf())

    val logs: StateFlow<List<Log>> get() = _logs

    override fun onDestroy() {
    }

    fun addLog(log: Log) {
        _logs.value += log
    }
}
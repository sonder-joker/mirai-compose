package com.youngerhousea.miraicompose

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

fun snapshotFlowSample() {
    fun counterPolicy(): SnapshotMutationPolicy<Int> = object : SnapshotMutationPolicy<Int> {
        override fun equivalent(a: Int, b: Int): Boolean = a == b
        override fun merge(previous: Int, current: Int, applied: Int) =
            current + (applied - previous)
    }

    val state = mutableStateOf(0, counterPolicy())
    val snapshot1 = Snapshot.takeMutableSnapshot()
    val snapshot2 = Snapshot.takeMutableSnapshot()
    try {
        snapshot1.enter {
            state.value += 10
        }
        snapshot2.enter {
            state.value += 20
        }
        snapshot1.apply().check()
        snapshot2.apply().check()
    } finally {
        snapshot1.dispose()
        snapshot2.dispose()
    }
}

fun main() {
    snapshotFlowSample()
}
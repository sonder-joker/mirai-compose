package com.youngerhousea.miraicompose.core.utils

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.Navigator
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.instancekeeper.InstanceKeeper
import com.arkivanov.decompose.instancekeeper.getOrCreate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import net.mamoe.mirai.Bot
import java.net.URL
import java.net.URLDecoder
import kotlin.collections.set
import kotlin.reflect.KProperty

@Suppress("NOTHING_TO_INLINE")
internal inline fun <T> Iterable<T>.replace(index: Int, item: T): List<T> {
    return mapIndexed { i, t -> if (i == index) item else t }
}

fun <C : Any> Navigator<C>.insertIfNotExist(configuration: C) {

}

fun <T1, T2, R> CoroutineScope.combineState(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    sharingStarted: SharingStarted = SharingStarted.Eagerly,
    transform: (T1, T2) -> R
): StateFlow<R> = combine(flow1, flow2) { o1, o2 ->
    transform.invoke(o1, o2)
}.stateIn(this, sharingStarted, transform.invoke(flow1.value, flow2.value))

fun <T1, T2, T3, R> CoroutineScope.combineState(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    flow3: StateFlow<T3>,
    sharingStarted: SharingStarted = SharingStarted.Eagerly,
    transform: (T1, T2, T3) -> R
): StateFlow<R> = combine(flow1, flow2, flow3) { o1, o2, o3 ->
    transform.invoke(o1, o2, o3)
}.stateIn(this, sharingStarted, transform.invoke(flow1.value, flow2.value, flow3.value))

inline val Bot.stringId get() = id.toString()


internal fun URL.splitQuery(): Map<String, String> {
    val queryPairs: MutableMap<String, String> = LinkedHashMap()
    val query: String = this.query
    val pairs = query.split("&".toRegex()).toTypedArray()
    for (pair in pairs) {
        val idx = pair.indexOf("=")
        queryPairs[URLDecoder.decode(pair.substring(0, idx), "UTF-8")] =
            URLDecoder.decode(pair.substring(idx + 1), "UTF-8")
    }
    return queryPairs
}


private class ComponentScope(
    private val scope: CoroutineScope = MainScope()
) : InstanceKeeper.Instance, CoroutineScope by scope {
    override fun onDestroy() {
        scope.cancel()
    }
}

internal fun ComponentContext.componentScope(): CoroutineScope = instanceKeeper.getOrCreate(::ComponentScope)


inline operator fun <reified T> MutableStateFlow<T>.setValue(thisRef: Any?, property: KProperty<*>, value: T) {
    this.value = value
}

inline operator fun <reified T> MutableStateFlow<T>.getValue(thisRef: Any?, property: KProperty<*>): T {
    return this.value
}

inline val <C : Any, T : Any> RouterState<C, T>.activeConfiguration get() = this.activeChild.configuration

inline val <C : Any, T : Any> RouterState<C, T>.activeInstance get() = this.activeChild.instance

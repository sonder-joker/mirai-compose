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

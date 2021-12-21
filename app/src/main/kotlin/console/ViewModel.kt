package com.youngerhousea.mirai.compose.console

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.youngerhousea.mirai.compose.console.impl.ViewModelStoreImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

interface ViewModel {
    fun onDestroy()
}

abstract class ViewModelScope : ViewModel {
    protected val viewModelScope: CoroutineScope = CoroutineScope(SupervisorJob())

    override fun onDestroy() {
        viewModelScope.cancel()
    }
}

interface ViewModelStore {
    fun put(key: Any, viewModel: ViewModel)

    fun get(key: Any): ViewModel?

    fun clean()
}

inline fun <reified T : ViewModel> ViewModelStore.get(): T? {
    return get(T::class) as T?
}

inline fun <reified T : ViewModel> ViewModelStore.getOrCreate(crossinline factory: () -> T): T {
    return getOrCreate(T::class, factory)
}

inline fun <reified T : ViewModel> ViewModelStore.getOrCreate(key: Any, crossinline factory: () -> T): T {
    return get(key) as? T ?: factory().apply { put(key, this) }
}

internal val viewModelStoreImpl = ViewModelStoreImpl()

val LocalViewModelStore = staticCompositionLocalOf<ViewModelStore> {
    viewModelStoreImpl
}

@Composable
inline fun <reified T : ViewModel> viewModel(): T? {
    return LocalViewModelStore.current.get()
}

@Composable
inline fun <reified T : ViewModel> viewModel(crossinline factory: () -> T): T {
    return LocalViewModelStore.current.getOrCreate(factory)
}

@Composable
inline fun <reified T : ViewModel> viewModel(key: Any, crossinline factory: () -> T): T {
    return LocalViewModelStore.current.getOrCreate(key, factory)
}
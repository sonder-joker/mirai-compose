package com.youngerhousea.mirai.compose.console.impl

import com.youngerhousea.mirai.compose.console.ViewModel
import com.youngerhousea.mirai.compose.console.ViewModelStore
import java.util.concurrent.ConcurrentHashMap

internal class ViewModelStoreImpl : ViewModelStore {
    private val hashMap: ConcurrentHashMap<Any, ViewModel> = ConcurrentHashMap()

    override fun put(key: Any, viewModel: ViewModel) {
        hashMap[key]?.onDestroy()

        hashMap[key] = viewModel
    }

    override fun get(key: Any): ViewModel? {
        return hashMap[key]
    }

    override fun clean() {
        hashMap.values.forEach(ViewModel::onDestroy)
        hashMap.clear()
    }
}
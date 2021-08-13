package com.youngerhousea.mirai.compose.console.impl

interface Lifecycle {

    val state: State

    fun subscribe(observer: Observer)

    fun unsubscribe(observer: Observer)

    enum class State {
        Initialized,
        Loading,
        AutoLogin,
        Live,
        Destroy
    }


    interface Observer {
        fun onEnterLoading() {}

        fun onFinishAutoLogin() {}

        fun onFinishLoading() {}

        fun onDestroy() {}
    }
}

interface LifecycleOwner {
    val lifecycle: Lifecycle
}

inline fun Lifecycle.doOnLoading(crossinline action: () -> Unit) {
    subscribe(object : Lifecycle.Observer {
        override fun onEnterLoading() {
            action()
        }
    })
}


inline fun Lifecycle.doOnFinishLoading(crossinline action: () -> Unit) {
    subscribe(object : Lifecycle.Observer {
        override fun onFinishLoading() {
            action()
        }
    })
}

inline fun Lifecycle.doOnFinishAutoLogin(crossinline action: () -> Unit) {
    subscribe(object : Lifecycle.Observer {
        override fun onFinishAutoLogin() {
            action()
        }
    })
}
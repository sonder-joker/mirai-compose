package com.youngerhousea.mirai.compose.console.impl

import java.lang.ref.WeakReference

internal interface LifecycleRegistry : Lifecycle, Lifecycle.Observer {
    override var state: Lifecycle.State

    val owner: WeakReference<LifecycleOwner>
}

internal class LifecycleRegistryImpl(override val owner: WeakReference<LifecycleOwner>) : LifecycleRegistry {
    private var callbacks = emptySet<Lifecycle.Observer>()

    override var state: Lifecycle.State = Lifecycle.State.Initialized

    override fun subscribe(observer: Lifecycle.Observer) {
        check(observer !in this.callbacks) { "Already subscribed" }
        this.callbacks += observer

        if (state >= Lifecycle.State.Loading) {
            observer.onEnterLoading()
        }
        if (state >= Lifecycle.State.AutoLogin) {
            observer.onFinishAutoLogin()
        }
        if (state >= Lifecycle.State.Live) {
            observer.onFinishLoading()
        }
    }

    override fun unsubscribe(observer: Lifecycle.Observer) {
        check(observer in this.callbacks) { "Not subscribed" }
        this.callbacks -= observer
    }

    override fun onEnterLoading() {
        checkState(Lifecycle.State.Initialized)
        callbacks.forEach(Lifecycle.Observer::onEnterLoading)
        state = Lifecycle.State.Loading
    }

    override fun onFinishAutoLogin() {
        checkState(Lifecycle.State.Loading)
        callbacks.forEach(Lifecycle.Observer::onFinishAutoLogin)
        state = Lifecycle.State.AutoLogin
    }

    override fun onFinishLoading() {
        checkState(Lifecycle.State.AutoLogin)
        callbacks.forEach(Lifecycle.Observer::onFinishLoading)
        state = Lifecycle.State.Live
    }

    override fun onDestroy() {
        checkState(Lifecycle.State.Live)
        callbacks.forEach(Lifecycle.Observer::onDestroy)
        state = Lifecycle.State.Destroy
    }

    private fun checkState(vararg required: Lifecycle.State) {
        check(state in required) { "Expected in state ($required) but was $state" }
    }

}
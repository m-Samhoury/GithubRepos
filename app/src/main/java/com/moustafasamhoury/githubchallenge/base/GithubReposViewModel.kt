package com.moustafasamhoury.githubchallenge.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @author moustafasamhoury
 * created on Saturday, 04 May, 2019
 */

abstract class GithubReposViewModel(protected val compositeDisposable: CompositeDisposable = CompositeDisposable()) :
    ViewModel(), LifecycleOwner {


    private val lifeCycleRegistry: LifecycleRegistry by lazy {
        val lifeCycleRegistry = LifecycleRegistry(this)

        lifeCycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        lifeCycleRegistry
    }

    override fun getLifecycle(): Lifecycle = lifeCycleRegistry

    /**
     * Adds the created disposable to be correctly disposed when the viewmodel onCleared method is invoked
     *
     * @param disposable the disposable to be disposed
     */
    protected fun add(disposable: Disposable) = compositeDisposable.add(disposable)


    override fun onCleared() {
        compositeDisposable.clear()
        lifeCycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        super.onCleared()
    }


    protected fun Disposable.disposeOnClear(): Disposable {
        compositeDisposable.add(this)
        return this
    }

}
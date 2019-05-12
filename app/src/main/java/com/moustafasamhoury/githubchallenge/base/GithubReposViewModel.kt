package com.moustafasamhoury.githubchallenge.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @author moustafasamhoury
 * created on Saturday, 04 May, 2019
 */

abstract class GithubReposViewModel(protected val compositeDisposable: CompositeDisposable = CompositeDisposable()) :
    ViewModel() {


    /**
     * Adds the created disposable to be correctly disposed when the viewmodel onCleared method is invoked
     *
     * @param disposable the disposable to be disposed
     */
    protected fun add(disposable: Disposable) = compositeDisposable.add(disposable)


    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }


    protected fun Disposable.disposeOnClear(): Disposable {
        compositeDisposable.add(this)
        return this
    }

}
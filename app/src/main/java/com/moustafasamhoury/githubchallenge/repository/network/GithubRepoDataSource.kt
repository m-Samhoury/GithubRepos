package com.moustafasamhoury.githubchallenge.repository.network

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.moustafasamhoury.githubchallenge.models.GithubRepo
import com.moustafasamhoury.githubchallenge.utils.NetworkResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author moustafasamhoury
 * created on Saturday, 11 May, 2019
 */

class GithubRepoDataSource(
    private val retrofitGithubService: RetrofitGithubService,
    private val createdAfterDate: String,
    private val networkState: MutableLiveData<NetworkState>
) :
    PageKeyedDataSource<Int, GithubRepo>() {
    companion object {
        val TAG = PageKeyedDataSource::class.java.name
    }

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null


    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    @SuppressLint("CheckResult")
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, GithubRepo>) {
        if (networkState.value == NetworkState.Loading) {
            Log.d(TAG, "tried to load in loadInitial multiple times")
            return
        }

        networkState.postValue(NetworkState.Loading)

        retrofitGithubService.topGithubRepositories(
            createdAfterDate = createdAfterDate,
            page = 1,
            itemsPerPage = params.requestedLoadSize
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                networkState.postValue(NetworkState.Loaded)
                retry = null

                when (val response = it) {
                    is NetworkResponse.Success -> {
                        callback.onResult(
                            response.body.items,
                            0,
                            if (params.placeholdersEnabled) response.body.total else response.body.items.size,
                            null,
                            2
                        )
                    }
                    is NetworkResponse.ServerError -> {
                        retry = {
                            loadInitial(params, callback)
                        }
                        val errors = response.body
                            ?.joinErrors()

                        networkState.postValue(NetworkState.Error(Throwable(errors ?: "unknown error"), retry))
                    }
                    is NetworkResponse.NetworkError -> {
                        retry = {
                            loadInitial(params, callback)
                        }
                        val throwable = response.error
                        networkState.postValue(NetworkState.Error(throwable, retry))

                    }
                }
            }, {
                retry = {
                    loadInitial(params, callback)
                }
                networkState.postValue(NetworkState.Error(it, retry))

            })


    }

    @SuppressLint("CheckResult")
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, GithubRepo>) {
        if (networkState.value == NetworkState.Loading) {
            Log.d(TAG, "tried to load in loadAfter multiple times")
            return
        }

        networkState.postValue(NetworkState.Loading)

        retrofitGithubService.topGithubRepositories(
            createdAfterDate = createdAfterDate,
            page = params.key,
            itemsPerPage = params.requestedLoadSize
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                networkState.postValue(NetworkState.Loaded)
                retry = null

                when (val response = it) {
                    is NetworkResponse.Success -> {
                        callback.onResult(
                            response.body.items,
                            params.key + 1
                        )
                    }
                    is NetworkResponse.ServerError -> {
                        retry = {
                            loadAfter(params, callback)
                        }
                        val errors = response.body
                            ?.joinErrors()
                        networkState.postValue(NetworkState.Error(Throwable(errors ?: "unknown error"), retry))

                    }
                    is NetworkResponse.NetworkError -> {
                        val throwable = response.error
                        retry = {
                            loadAfter(params, callback)
                        }
                        networkState.postValue(NetworkState.Error(throwable, retry))
                    }
                }
            }, {
                retry = {
                    loadAfter(params, callback)
                }
                networkState.postValue(NetworkState.Error(it, retry))

            })


    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, GithubRepo>) {
        // ignored, since we only ever append to our initial load
    }


}
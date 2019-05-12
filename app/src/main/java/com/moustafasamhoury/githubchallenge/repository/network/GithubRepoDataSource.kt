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

public class GithubRepoDataSource(
    private val retrofitGithubService: RetrofitGithubService,
    private val createdAfterDate: String
) :
    PageKeyedDataSource<Int, GithubRepo>() {
    companion object {
        val TAG = PageKeyedDataSource::class.java.name
    }

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter.
     */
    val networkState = MutableLiveData<NetworkState>()


    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    @SuppressLint("CheckResult")
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, GithubRepo>) {
        if (networkState.value == NetworkState.LOADING) {
            Log.d(TAG,"tried to load in loadInitial multiple times")
            return
        }

        networkState.postValue(NetworkState.LOADING)

        retrofitGithubService.topGithubRepositories(
            createdAfterDate = createdAfterDate,
            page = 1,
            itemsPerPage = params.requestedLoadSize
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                networkState.postValue(NetworkState.LOADED)
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
                        networkState.postValue(NetworkState.error(errors))

//                        networkState.postValue(PaginationNetworkState.Error(errors ?: "unknown error"))
                    }
                    is NetworkResponse.NetworkError -> {
                        val throwable = response.error
//                        retry = {
//                            loadInitial(params, callback)
//                        }
                        val error = NetworkState.error(throwable.toString())
                        networkState.postValue(error)

//                        networkState.postValue(PaginationNetworkState.Error(throwable.toString()))

                    }
                }
            }, {
                //                retry = {
//                    loadInitial(params, callback)
//                }
                val error = NetworkState.error(it.message ?: "unknown error")
                networkState.postValue(error)
//                networkState.postValue(PaginationNetworkState.Error(it.toString()))

            })


    }

    @SuppressLint("CheckResult")
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, GithubRepo>) {
        if (networkState.value == NetworkState.LOADING) {
            Log.d(TAG,"tried to load in loadAfter multiple times")
            return
        }

        networkState.postValue(NetworkState.LOADING)

        retrofitGithubService.topGithubRepositories(
            createdAfterDate = createdAfterDate,
            page = params.key,
            itemsPerPage = params.requestedLoadSize
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                networkState.postValue(NetworkState.LOADED)
                retry = null

                when (val response = it) {
                    is NetworkResponse.Success -> {
                        callback.onResult(
                            response.body.items,
                            params.key + 1
                        )
                    }
                    is NetworkResponse.ServerError -> {
                        val errors = response.body
                            ?.joinErrors()
//                        retry = {
//                            loadAfter(params, callback)
//                        }
                        networkState.postValue(NetworkState.error(it.toString()))

                    }
                    is NetworkResponse.NetworkError -> {
                        val throwable = response.error
                        retry = {
                            loadAfter(params, callback)
                        }
                        val error = NetworkState.error(throwable.message ?: "unknown error")
                        networkState.postValue(error)

                    }
                }
            }, {
                //                retry = {
//                    loadAfter(params, callback)
//                }
                networkState.postValue(NetworkState.error(it.toString()))

            })


    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, GithubRepo>) {
        // ignored, since we only ever append to our initial load
    }


}
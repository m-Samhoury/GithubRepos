package com.moustafasamhoury.githubchallenge.repository.network

import androidx.paging.PagedList
import com.moustafasamhoury.githubchallenge.models.GithubRepo

/**
 * @author moustafasamhoury
 * created on Friday, 10 May, 2019
 */

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: Status,
    val msg: String? = null,
    val body: PagedList<GithubRepo>? = null
) {
    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
//        fun InitialSuccess(pagedList: PagedList<GithubRepo>) = NetworkState(Status.SUCCESS, body = pagedList)
        val LOADING = NetworkState(Status.RUNNING)
        fun error(msg: String?) = NetworkState(Status.FAILED, msg)
    }
}

//sealed class PaginationNetworkState<out T> {
//    data class PagedListReady<out T>(val pagedList: T) : PaginationNetworkState<T>()
//    object Loading : PaginationNetworkState<Nothing>()
//    object Loaded : PaginationNetworkState<Nothing>()
//    data class Error(val errorMessage: String) : PaginationNetworkState<Nothing>()
//}






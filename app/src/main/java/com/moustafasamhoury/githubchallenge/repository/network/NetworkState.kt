package com.moustafasamhoury.githubchallenge.repository.network

/**
 * @author moustafasamhoury
 * created on Friday, 10 May, 2019
 */

sealed class NetworkState {
    object Loading : NetworkState()
    object Loaded : NetworkState()
    data class Error(val throwable: Throwable) : NetworkState()
}





package com.moustafasamhoury.githubchallenge.repository.network

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.moustafasamhoury.githubchallenge.models.GithubRepo

/**
 * @author moustafasamhoury
 * created on Saturday, 11 May, 2019
 */


/**
 * A simple data source factory which also provides a way to observe the last created data source.
 * This allows us to channel its network request status etc back to the UI. See the Listing creation
 * in the Repository class.
 */
class GithubRepoDataSourceFactory(
    private val service: RetrofitGithubService,
    private val createdAfterDate: String,
    private val networkState: MutableLiveData<NetworkState>
) : DataSource.Factory<Int, GithubRepo>() {
    init {
        create()
    }

    override fun create(): DataSource<Int, GithubRepo> {
        val source = GithubRepoDataSource(service, createdAfterDate, networkState)
        return source
    }

    companion object {
        fun create(
            service: RetrofitGithubService, createdAfterDate: String,
            errorsLiveData: MutableLiveData<NetworkState>
        ) =
            GithubRepoDataSourceFactory(service, createdAfterDate, errorsLiveData)
    }
}

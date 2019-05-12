package com.moustafasamhoury.githubchallenge.repository

import androidx.paging.PagedList
import androidx.paging.toObservable
import com.moustafasamhoury.githubchallenge.models.GithubRepo
import com.moustafasamhoury.githubchallenge.repository.network.GithubRepoDataSourceFactory
import com.moustafasamhoury.githubchallenge.repository.network.RetrofitGithubService
import io.reactivex.Observable

/**
 * This repository is our source of truth, we fetch all the data using this repository
 * It may fetch the required data from the network layer or from the local db layer
 *
 * @author moustafasamhoury
 * created on Wednesday, 01 May, 2019
 */
class Repository(private val service: RetrofitGithubService) {


    fun fetchTopGithubRepositoriesPaginated(
        createdAfterDate: String
    ): Observable<PagedList<GithubRepo>> {

        val sourceFactory =
            GithubRepoDataSourceFactory.create(service, createdAfterDate)
        val sourceFactoryObservable = sourceFactory.toObservable(
            config = PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(20)
                .setPrefetchDistance(2)
                .setMaxSize(10 + 2 * 2)
                .setPageSize(10)
                .build()
        )

        return sourceFactoryObservable
    }
}
 
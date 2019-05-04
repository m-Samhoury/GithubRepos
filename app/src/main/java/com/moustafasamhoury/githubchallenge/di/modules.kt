package com.moustafasamhoury.githubchallenge.di

import com.moustafasamhoury.githubchallenge.BuildConfig
import com.moustafasamhoury.githubchallenge.features.reposlist.ReposListViewModel
import com.moustafasamhoury.githubchallenge.repository.Repository
import com.moustafasamhoury.githubchallenge.repository.network.RetrofitGithubService
import com.moustafasamhoury.githubchallenge.repository.network.RetrofitWebServiceFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author moustafasamhoury
 * created on Thursday, 02 May, 2019
 */

val repositoryModule: Module = module {

    single<RetrofitGithubService> {
        RetrofitWebServiceFactory.makeServiceFactory(
            RetrofitWebServiceFactory.makeHttpClient(androidContext()), BuildConfig.BASE_API_URL
        )
    }

    single { Repository(get()) }

}

val viewModelsModule = module {
    viewModel { ReposListViewModel(get()) }
}
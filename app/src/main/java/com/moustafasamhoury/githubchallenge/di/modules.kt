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
    single { RetrofitWebServiceFactory.makeHttpClient(androidContext()) }

    single<RetrofitGithubService> { RetrofitWebServiceFactory.makeServiceFactory(get()) }
    single { RetrofitWebServiceFactory.makeRetrofit(BuildConfig.BASE_API_URL, get()) }
    single { Repository(get()) }

}

val viewModelsModule = module {
    viewModel {
        ReposListViewModel(repository = get())
    }
}
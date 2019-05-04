package com.moustafasamhoury.githubchallenge

import android.app.Application
import com.moustafasamhoury.githubchallenge.di.repositoryModule
import com.moustafasamhoury.githubchallenge.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * @author moustafasamhoury
 * created on Wednesday, 01 May, 2019
 */

class GithubReposApplication : Application() {


    override fun onCreate() {
        super.onCreate()


        startKoin {
            // declare used Android context
            androidContext(this@GithubReposApplication)

            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            androidFileProperties()


            modules(repositoryModule, viewModelsModule)
        }

    }
}
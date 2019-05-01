package com.moustafasamhoury.githubchallenge.repository.network

import android.content.Context
import okhttp3.Cache
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * This helper class provide methods to setup retrofit in the application
 *
 * @author moustafasamhoury
 * created on Wednesday, 01 May, 2019
 */

object RetrofitWebServiceFactory {
    private const val TIME_OUTS: Long = 40L

    fun makeHttpClient(context: Context): OkHttpClient =
            makeHttpClientBuilder(context)
                    .build()

    fun makeHttpClientBuilder(context: Context): OkHttpClient.Builder =
            OkHttpClient.Builder()
                    .connectTimeout(TIME_OUTS, TimeUnit.SECONDS)
                    .readTimeout(TIME_OUTS, TimeUnit.SECONDS)
                    .writeTimeout(TIME_OUTS, TimeUnit.SECONDS)
                    .certificatePinner(CertificatePinner.DEFAULT)
                    .cache(Cache(context.cacheDir, 10 * 1024 * 1024))
                    .retryOnConnectionFailure(false)

    inline fun <reified T> makeServiceFactory(
            context: Context,
            okHttpClient: OkHttpClient,
            baseUrl: String
    ): T {

        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build()
        return retrofit.create(T::class.java)

    }

}
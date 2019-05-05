package com.moustafasamhoury.githubchallenge.utils

import io.reactivex.*
import io.reactivex.functions.Function
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type
import kotlin.reflect.KClass

/**
 * @author moustafasamhoury
 * created on Saturday, 04 May, 2019
 */

/**
 * Describes the class for the http error body to be parsed using the deserializer library
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class ErrorType(val type: KClass<*>)

class NetworkException(
    val error: Any?,
    override val message: String = "",
    val throwable: Throwable
) : RuntimeException()

class RxCallAdapterWrapperFactory constructor(private val rxJava2CallAdapterFactory: RxJava2CallAdapterFactory) :
    CallAdapter.Factory() {

    companion object {
        fun create(): RxCallAdapterWrapperFactory {
            return RxCallAdapterWrapperFactory(RxJava2CallAdapterFactory.create())
        }
    }

    private fun handleError(annotations: Array<Annotation>, retrofit: Retrofit, throwable: Throwable): Throwable {

        val errorType: ErrorType? = annotations.find { it is ErrorType } as? ErrorType

        return if (errorType != null && throwable is HttpException) {
            val error = parseError(retrofit, throwable, errorType.type)
            NetworkException(error, throwable.message(), throwable)
        } else throwable
    }

    private fun parseError(retrofit: Retrofit, httpException: HttpException, kClass: KClass<*>): Any? {
        if (httpException.response().isSuccessful) {
            return null
        }
        val errorBody = httpException.response().errorBody() ?: return null
        val converter: Converter<ResponseBody, Any> = retrofit.responseBodyConverter(kClass.java, arrayOf())
        return converter.convert(errorBody)
    }

    private inner class RxCallAdapterWrapper constructor(
        private val annotations: Array<Annotation>,
        private val retrofit: Retrofit,
        private val callAdapter: CallAdapter<Any, Any>
    ) : CallAdapter<Any, Any> {

        override fun adapt(call: Call<Any>): Any = when (val any = callAdapter.adapt(call)) {
            is Observable<*> ->
                any.onErrorResumeNext(Function { Observable.error(handleError(annotations, retrofit, it)) })
            is Maybe<*> ->
                any.onErrorResumeNext(Function { Maybe.error(handleError(annotations, retrofit, it)) })
            is Single<*> ->
                any.onErrorResumeNext(Function { Single.error(handleError(annotations, retrofit, it)) })
            is Flowable<*> ->
                any.onErrorResumeNext(Function { Flowable.error(handleError(annotations, retrofit, it)) })
            is Completable ->
                any.onErrorResumeNext(Function { Completable.error(handleError(annotations, retrofit, it)) })
            else -> any
        }


        override fun responseType(): Type {
            return callAdapter.responseType()
        }
    }

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        @Suppress("UNCHECKED_CAST")
        val rxJava2CallAdapter: CallAdapter<Any, Any> =
            rxJava2CallAdapterFactory.get(returnType, annotations, retrofit) as CallAdapter<Any, Any>
        return RxCallAdapterWrapper(annotations, retrofit, rxJava2CallAdapter)
    }
}
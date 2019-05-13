package com.moustafasamhoury.githubchallenge.utils

/**
 * @author moustafasamhoury
 * created on Tuesday, 07 May, 2019
 */

import com.squareup.moshi.Types
import io.reactivex.*
import io.reactivex.functions.Function
import okhttp3.ResponseBody
import retrofit2.*
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * A [CallAdapter.Factory] which allows [NetworkResponse] objects to be returned from RxJava
 * streams.
 *
 * Adding this class to [Retrofit] allows you to return [Observable], [Flowable], [Single], or
 * [Maybe] types parameterized with [NetworkResponse] from service methods.
 *
 * Note: This adapter must be registered before an adapter that is capable of adapting RxJava
 * streams.
 */
class KotlinRxJava2CallAdapterFactory private constructor() : CallAdapter.Factory() {

    companion object {
        @JvmStatic
        fun create() = KotlinRxJava2CallAdapterFactory()
    }

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val rawType = getRawType(returnType)

        val isFlowable = rawType === Flowable::class.java
        val isSingle = rawType === Single::class.java
        val isMaybe = rawType === Maybe::class.java
        if (rawType !== Observable::class.java && !isFlowable && !isSingle && !isMaybe) {
            return null
        }

        if (returnType !is ParameterizedType) {
            throw IllegalStateException(
                "${rawType.simpleName} return type must be parameterized as " +
                        "${rawType.simpleName}<Foo> or ${rawType.simpleName}<? extends Foo>"
            )
        }

        val observableEmissionType = getParameterUpperBound(0, returnType)
        if (getRawType(observableEmissionType) != NetworkResponse::class.java) {
            return null
        }

        if (observableEmissionType !is ParameterizedType) {
            throw IllegalStateException(
                "NetworkResponse must be parameterized as NetworkResponse<SuccessBody, ErrorBody>"
            )
        }

        val successBodyType = getParameterUpperBound(0, observableEmissionType)
        val delegateType = Types.newParameterizedType(
            Observable::class.java,
            successBodyType
        )
        val delegateAdapter = retrofit.nextCallAdapter(
            this,
            delegateType,
            annotations
        )

        val errorBodyType = getParameterUpperBound(1, observableEmissionType)
        val errorBodyConverter = retrofit.nextResponseBodyConverter<Any>(
            null,
            errorBodyType,
            annotations
        )

        @Suppress("UNCHECKED_CAST") // Type of delegateAdapter is not known at compile time.
        return KotlinRxJava2CallAdapter(
            successBodyType,
            delegateAdapter as CallAdapter<Any, Observable<Any>>,
            errorBodyConverter,
            isFlowable,
            isSingle,
            isMaybe
        )
    }
}


/**
 * Represents the result of making a network request.
 *
 * @param T success body type for 2xx response.
 * @param U error body type for non-2xx response.
 */
sealed class NetworkResponse<out T : Any, out U : Any> {

    /**
     * A request that resulted in a response with a 2xx status code that has a body.
     */
    data class Success<T : Any>(val body: T) : NetworkResponse<T, Nothing>()

    /**
     * A request that resulted in a response with a non-2xx status code.
     */
    data class ServerError<U : Any>(val body: U?, val code: Int) : NetworkResponse<Nothing, U>()

    /**
     * A request that didn't result in a response.
     */
    data class NetworkError(val error: IOException) : NetworkResponse<Nothing, Nothing>()


}


internal class KotlinRxJava2CallAdapter<T : Any, U : Any>(
    private val successBodyType: Type,
    private val delegateAdapter: CallAdapter<T, Observable<T>>,
    private val errorConverter: Converter<ResponseBody, U>,
    private val isFlowable: Boolean,
    private val isSingle: Boolean,
    private val isMaybe: Boolean
) : CallAdapter<T, Any> {

    override fun adapt(call: Call<T>): Any =
        delegateAdapter.adapt(call)
            .flatMap {
                Observable.just<NetworkResponse<T, U>>(NetworkResponse.Success(it))
            }
            .onErrorResumeNext(
                Function<Throwable, Observable<NetworkResponse<T, U>>> { throwable ->
                    when (throwable) {
                        is HttpException -> {
                            val error = throwable.response().errorBody()
                            val errorBody = when {
                                error == null -> null
                                error.contentLength() == 0L -> null
                                else -> {
                                    try {
                                        errorConverter.convert(error)
                                    } catch (e: Exception) {
                                        return@Function Observable.just(
                                            NetworkResponse.NetworkError(
                                                IOException(
                                                    "Couldn't deserialize error body: ${error.string()}",
                                                    e
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                            val serverError = NetworkResponse.ServerError(
                                errorBody,
                                throwable.response().code()
                            )
                            Observable.just(serverError)
                        }
                        is IOException -> {
                            Observable.just(
                                NetworkResponse.NetworkError(
                                    throwable
                                )
                            )
                        }
                        else -> {
                            throw throwable
                        }
                    }
                }).run {
                when {
                    isFlowable -> this.toFlowable(BackpressureStrategy.LATEST)
                    isSingle -> this.singleOrError()
                    isMaybe -> this.singleElement()
                    else -> this
                }
            }

    override fun responseType(): Type = successBodyType
}
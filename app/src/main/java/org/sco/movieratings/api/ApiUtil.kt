package org.sco.movieratings.api

import retrofit2.Response
import java.io.IOException

suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): Result<T> {
    val response: Response<T>
    try {
        response = call.invoke()
    } catch (t: Throwable) {
        return Result.failure(t)
    }

    return if (!response.isSuccessful) {
        val errorBody = response.errorBody()
        @Suppress("BlockingMethodInNonBlockingContext")
        Result.failure(IOException("Bad Response: ${response.message()} ${response.code()} ${errorBody?.string()}"))
    } else {
        val responseBody = response.body()
        return if (responseBody == null) {
            Result.failure(IllegalStateException("response.body() was null"))
        } else {
            Result.success(responseBody)
        }
    }
}
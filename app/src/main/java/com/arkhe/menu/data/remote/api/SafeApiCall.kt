package com.arkhe.menu.data.remote.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> T
): SafeApiResult<T> {
    return withContext(Dispatchers.IO) {
        try {
            val result = apiCall()
            SafeApiResult.Success(result)
        } catch (exception: Exception) {
            val networkException = NetworkErrorHandler.handleException(exception)
            SafeApiResult.Error(networkException)
        }
    }
}